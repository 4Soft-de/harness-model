/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =========================LICENSE_END==================================
 */
package com.foursoft.harness.kbl2vec.core;

import com.foursoft.harness.kbl2vec.convert.ConverterRegistry;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class ConversionOrchestrator<S, D> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConversionOrchestrator.class);
    private final Class<S> sourceClass;
    private final Class<D> destinationClass;
    private final TransformerRegistry transformerRegistry;
    private final TransformationContext transformationContext;

    private final Queue<TransformationHolder> transformations = new ConcurrentLinkedQueue<>();

    private final Queue<FinisherHolder> finisher = new ConcurrentLinkedQueue<>();

    private final List<Processor<D>> postProcessors = new ArrayList<>();

    private final List<Processor<S>> preProcessors = new ArrayList<>();

    private final Map<Object, String> comments = new HashMap<>();

    public ConversionOrchestrator(final Class<S> sourceRootClass, final Class<D> destinationRootClass,
                                  final TransformerRegistry transformerRegistry,
                                  final ConversionProperties conversionProperties
    ) {
        this.sourceClass = sourceRootClass;
        this.destinationClass = destinationRootClass;
        this.transformerRegistry = transformerRegistry;
        this.transformationContext = new TransformationContextImpl(conversionProperties,
                                                                   new ConverterRegistry(conversionProperties),
                                                                   new EntityMapping());
        LOGGER.debug("Created orchestrator for conversion pipeline.");
    }

    public Result<D> orchestrateTransformation(final S source) {
        LOGGER.debug("Starting conversion for: {}", source);

        final S innerSource = handleProcessorsPipeline(preProcessors, source);

        final AtomicReference<D> resultReference = new AtomicReference<>();

        final Transformation<S, D> initialTransformation = new Transformation<>(sourceClass, destinationClass,
                                                                                Query.of(innerSource),
                                                                                resultReference::set);

        transformations.offer(new TransformationHolder(initialTransformation, TransformationStackTrace.empty()));

        processTransformations();

        processFinalizer();

        final D resultValue = handleProcessorsPipeline(postProcessors, resultReference.get());

        if (resultValue == null) {
            throw new ConversionException("No result found.");
        }

        return new Result<>(resultValue, comments, this.transformationContext.getEntityMapping().getContent());
    }

    public void addPostProcessor(final Processor<D> postProcessor) {
        this.postProcessors.add(postProcessor);
    }

    public void addPreProcessor(final Processor<S> preProcessor) {
        this.preProcessors.add(preProcessor);
    }

    private void processFinalizer() {
        FinisherHolder finisherHolder;
        while ((finisherHolder = finisher.poll()) != null) {
            try {
                finisherHolder.finisher().finishTransformation(this.transformationContext);
            } catch (final Exception e) {
                throw new FinisherException(finisherHolder.finisher(), finisherHolder.currentStackTrace(), e);
            }
        }
    }

    private void processTransformations() {
        TransformationHolder currentHolder = null;
        while ((currentHolder = transformations.poll()) != null) {
            handleSingleTransformation(currentHolder.transformation(), currentHolder.currentStackTrace());
        }
    }

    private <T> T handleProcessorsPipeline(final List<Processor<T>> processors, final T initialValue) {
        // One Handling Map for Post & Pre Processing
        // TODO: This does not ensure immutablity.
        //  If Immutability (or backreferencing) is required,it is the responsibility of the processor.
        // On the other hand, imutability would invalidate the context (e.g. entityMapping).
        // However, this should not be an issue, as the entityMapping should only be relevant to
        // Transformer which are run before or after the processor pipeline.

        T resultValue = initialValue;

        for (final Processor<T> processor : processors) {
            resultValue = processor.apply(initialValue, transformationContext);
        }
        return resultValue;
    }

    private <FROM, TO> void handleSingleTransformation(final Transformation<FROM, TO> transformation,
                                                       final TransformationStackTrace parentStackTrace) {
        new TransformationHandler<>(transformation, parentStackTrace).handle();
    }

    private static List<TransformationHolder> wrapTransformations(
            final Collection<Transformation<?, ?>> transformations,
            final TransformationStackTrace currentStackTrace) {
        return transformations.stream().map(t -> new TransformationHolder(t, currentStackTrace))
                .toList();
    }

    private static List<FinisherHolder> wrapFinishers(final Collection<Finisher> finishers,
                                                      final TransformationStackTrace currentStackTrace) {
        return finishers.stream().map(f -> new FinisherHolder(f, currentStackTrace))
                .toList();
    }

    /**
     * Handles a single queued {@link Transformation}: it runs the registered transformers for every source element,
     * optionally deduplicates the produced destination objects, records the source&rarr;destination entity mapping,
     * enqueues downstream transformations and finishers, and accumulates the (canonical) results into their parent.
     * <p>
     * One instance is created per queued transformation. The data that belongs to that single transformation - the
     * transformation itself, its stack trace, the matching transformers and the deduplication cache - is kept as
     * instance state instead of being threaded through the handling methods as parameters.
     */
    private final class TransformationHandler<FROM, TO> {

        private final Transformation<FROM, TO> transformation;
        private final TransformationStackTrace parentStackTrace;
        private final Collection<Transformer<FROM, TO>> transformers;
        // Canonical instance per deduplication key, scoped to this handler (i.e. this single transformation).
        // Only used when the transformation declares a deduplication key; it stays empty for the default path.
        private final Map<Object, TO> canonicalByKey = new HashMap<>();

        private TransformationHandler(final Transformation<FROM, TO> transformation,
                                      final TransformationStackTrace parentStackTrace) {
            this.transformation = transformation;
            this.parentStackTrace = parentStackTrace;
            this.transformers = transformerRegistry.getTransformer(transformation.sourceClass(),
                                                                   transformation.destinationClass());
        }

        private void handle() {
            transformation.sourceQuery()
                    .stream()
                    .flatMap(this::transformElement)
                    .filter(Objects::nonNull)
                    .forEach(transformation.accumulator());
        }

        private Stream<TO> transformElement(final FROM element) {
            return transformers.stream()
                    .map(transformer -> applyTransformer(transformer, element));
        }

        private TO applyTransformer(final Transformer<FROM, TO> transformer, final FROM element) {
            final TransformationResult<TO> result;
            try {
                result = transformer.transform(transformationContext, element);
            } catch (final Exception e) {
                throw new TransformerException(transformer, element, parentStackTrace, e);
            }

            if (result.isEmpty()) {
                return null;
            }

            final TO produced = result.element();
            final TO canonical = resolveCanonical(produced);
            if (canonical != produced) {
                // Duplicate: fold it into the retained instance (if a merger is defined) and re-point the source's
                // entity mapping to the canonical instance so second-phase linking resolves to it. The duplicate is
                // never wired into the graph (returning null suppresses accumulation) and its downstream
                // transformations and finishers are intentionally not enqueued.
                if (transformation.merger() != null) {
                    transformation.merger().accept(canonical, produced);
                }
                transformationContext.getEntityMapping().put(element, canonical);
                return null;
            }

            final TransformationStackTrace currentStackTrace = parentStackTrace.addElement(element, produced,
                                                                                           transformer);
            transformations.addAll(wrapTransformations(result.downstreamTransformations(), currentStackTrace));
            finisher.addAll(wrapFinishers(result.finisher(), currentStackTrace));
            transformationContext.getEntityMapping().put(element, produced);
            comments.putAll(result.comments());
            return produced;
        }

        /**
         * Resolves the canonical destination instance for a produced element. When the transformation does not
         * deduplicate, the produced element is always its own canonical instance. Otherwise the first element seen
         * per key becomes canonical; later elements with the same key resolve to that earlier instance.
         */
        private TO resolveCanonical(final TO produced) {
            if (transformation.deduplicationKey() == null) {
                return produced;
            }
            final Object key = transformation.deduplicationKey().apply(produced);
            final TO existing = canonicalByKey.putIfAbsent(key, produced);
            return existing == null ? produced : existing;
        }
    }

    public record Result<D>(D resultValue, Map<Object, String> comments, Multimap<Object, Object> entityMapping) {
    }

    private record TransformationHolder(Transformation<?, ?> transformation,
                                        TransformationStackTrace currentStackTrace) {
    }

    private record FinisherHolder(Finisher finisher, TransformationStackTrace currentStackTrace) {
    }

}
