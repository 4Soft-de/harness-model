package com.foursoft.harness.kbl2vec.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

public class ConversionOrchestrator<S, D> implements TransformationContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConversionOrchestrator.class);
    private final Class<S> sourceClass;
    private final Class<D> destinationClass;
    private final TransformerRegistry transformerRegistry;
    private final ConversionProperties conversionProperties;

    private final EntityMapping entityMapping = new EntityMapping();

    private final Queue<Transformation<?, ?>> transformations = new ConcurrentLinkedQueue<>();

    private final Queue<Finalizer> finalizer = new ConcurrentLinkedQueue<>();

    private final List<Processor<D>> postProcessors = new ArrayList<>();

    private final List<Processor<S>> preProcessors = new ArrayList<>();

    public ConversionOrchestrator(final Class<S> sourceRootClass, final Class<D> destinationRootClass,
                                  final TransformerRegistry transformerRegistry,
                                  final ConversionProperties conversionProperties
    ) {
        this.sourceClass = sourceRootClass;
        this.destinationClass = destinationRootClass;
        this.transformerRegistry = transformerRegistry;
        this.conversionProperties = conversionProperties;

        LOGGER.debug("Created orchestrator for conversion pipeline.");
    }

    public D convert(final S source) {
        LOGGER.debug("Starting conversion for: {}", source);

        final S innerSource = handleProcessorsPipeline(preProcessors, source);

        final AtomicReference<D> resultReference = new AtomicReference<>();

        final Transformation<S, D> initialTransformation = new Transformation<>(sourceClass, destinationClass,
                                                                                Query.of(innerSource),
                                                                                resultReference::set);

        transformations.offer(initialTransformation);

        processTransformationSteps();

        processFinalizer();

        final D resultValue = handleProcessorsPipeline(postProcessors, resultReference.get());

        if (resultValue == null) {
            throw new ConversionException("No result found.");
        }

        return resultValue;
    }

    public void addPostProcessor(final Processor<D> postProcessor) {
        this.postProcessors.add(postProcessor);
    }

    public void addPreProcessor(final Processor<S> preProcessor) {
        this.preProcessors.add(preProcessor);
    }

    private void processFinalizer() {
        Finalizer currentStep;
        while ((currentStep = finalizer.poll()) != null) {
            currentStep.finishTransformation(this);
        }
    }

    private void processTransformationSteps() {
        Transformation<?, ?> currentStep;
        while ((currentStep = transformations.poll()) != null) {
            handleElementTransformation(currentStep);
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

    private <FROM, TO> void handleElementTransformation(final Transformation<FROM, TO> step) {
        final Transformer<FROM, TO> transformer = transformerRegistry.getTransformer(this, step.sourceClass(),
                                                                                     step.destinationClass());

        step.sourceQuery()
                .stream()
                .map(from -> handleElementTransformation(transformer, from))
                .filter(Objects::nonNull)
                .forEach(step.contextLinker());

    }

    private <FROM, TO> TO handleElementTransformation(final Transformer<FROM, TO> transformer, final FROM element) {
        final TransformationResult<TO> result = transformer.transform(element);
        if (!result.isEmpty()) {
            transformations.addAll(result.downstreamTransformations());
            finalizer.addAll(result.finalizer());
            entityMapping.put(element, result.element());
        }
        return result.element();
    }

    @Override
    public EntityMapping getEntityMapping() {
        return entityMapping;
    }

    @Override
    public ConversionProperties getConversionProperties() {
        return conversionProperties;
    }
}
