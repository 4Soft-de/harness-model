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

public class ConversionOrchestrator<S, D> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConversionOrchestrator.class);
    private final Class<S> sourceClass;
    private final Class<D> destinationClass;
    private final TransformerRegistry transformerRegistry;
    private final TransformationContext transformationContext;

    private final Queue<Transformation<?, ?>> transformations = new ConcurrentLinkedQueue<>();

    private final Queue<Finalizer> finalizer = new ConcurrentLinkedQueue<>();

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

        transformations.offer(initialTransformation);

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
        Finalizer currentFinalizer;
        while ((currentFinalizer = finalizer.poll()) != null) {
            currentFinalizer.finishTransformation(this.transformationContext);
        }
    }

    private void processTransformations() {
        Transformation<?, ?> currentTransformation;
        while ((currentTransformation = transformations.poll()) != null) {
            handleSingleTransformation(currentTransformation);
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
            resultValue = processor.apply(initialValue);
        }
        return resultValue;
    }

    private <FROM, TO> void handleSingleTransformation(final Transformation<FROM, TO> transformation) {
        final Transformer<FROM, TO> transformer = transformerRegistry.getTransformer(transformation.sourceClass(),
                                                                                     transformation.destinationClass());

        transformation.sourceQuery()
                .stream()
                .map(from -> handleElementTransformation(transformer, from))
                .filter(Objects::nonNull)
                .forEach(transformation.accumulator());

    }

    private <FROM, TO> TO handleElementTransformation(final Transformer<FROM, TO> transformer, final FROM element) {
        final TransformationResult<TO> result = transformer.transform(this.transformationContext, element);
        if (!result.isEmpty()) {
            transformations.addAll(result.downstreamTransformations());
            finalizer.addAll(result.finalizer());
            transformationContext.getEntityMapping().put(element, result.element());
            comments.putAll(result.comments());
        }
        return result.element();
    }

    public record Result<D>(D resultValue, Map<Object, String> comments, Multimap<Object, Object> entityMapping) {
    }

}
