package de.foursoft.harness.xml.postprocessing;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.foursoft.harness.xml.cache.SimpleCache;

public class ModelPostProcessorRegistry {

    private SimpleCache<Class<?>, ModelPostProcessor> cachedModelPostProcessorFactory;

    private final SimpleCache<Class<?>, List<ModelPostProcessor>> modelPostProcessors = new SimpleCache<>(
            this::createProcessorList);

    private final List<ModelPostProcessor> defaultPostProcessor = new LinkedList<>();

    private final String contextPath;

    public ModelPostProcessorRegistry(final String contextPath) {
        this.contextPath = contextPath;
    }

    /**
     * 
     * @param postProcessor
     * @return
     */
    public ModelPostProcessorRegistry addDefaultPostProcessor(final ModelPostProcessor postProcessor) {
        defaultPostProcessor.add(postProcessor);
        return this;
    }

    /**
     * Register a factory function that is called once for each class to create
     * an individual {@link ModelPostProcessor} instance for the given class.
     * The {@link ModelPostProcessor} shall handle the necessary aspects of the
     * given class only, for inherited aspects of super classes an individual
     * {@link ModelPostProcessor} is created.
     * 
     * @param cachedModelPostProcessorFactory
     * @return
     */
    public ModelPostProcessorRegistry withFactory(
            final Function<Class<?>, ModelPostProcessor> modelPostProcessorFactory) {
        this.cachedModelPostProcessorFactory = new SimpleCache<>(modelPostProcessorFactory::apply);
        return this;
    }

    /**
     * Clear the state of all {@link ModelPostProcessor} currently registered,
     * to allow garbage collection and reuse during multiple unmarshallings.
     * 
     * @see ModelPostProcessor#clearState()
     */
    public void clearStateOfPostProcessors() {
        cachedModelPostProcessorFactory.getAllLoadedValues()
                .forEach(ModelPostProcessor::clearState);

        defaultPostProcessor.forEach(ModelPostProcessor::clearState);
    }

    /**
     * Finds all registered default {@link ModelPostProcessor}s that are an
     * instance of <tt>postProcessorType</tt>.
     * 
     * @param postProcessorType
     * @return
     */
    public <T extends ModelPostProcessor> List<T> findModelPostProcessors(final Class<T> postProcessorType) {
        return defaultPostProcessor.stream()
                .filter(postProcessorType::isInstance)
                .map(postProcessorType::cast)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of all {@link ModelPostProcessor} that are registered in
     * this registry and that are applicable for <tt>classToHandle</tt>.
     * <p/>
     * Applicable {@link ModelPostProcessor}s are basically all that satisfy
     * 
     * <tt>{@link ModelPostProcessor#getClassToHandle()}.isAssignableFrom(classToHandle)</tt>.
     * 
     * @param classToHandle
     * @return
     */
    public List<ModelPostProcessor> postProcessorsFor(final Class<?> classToHandle) {
        return modelPostProcessors.get(classToHandle);
    }

    private List<ModelPostProcessor> createProcessorList(final Class<?> classToHandle) {
        final List<ModelPostProcessor> returnValue = new LinkedList<>();

        for (final ModelPostProcessor processor : defaultPostProcessor) {
            if (processor.getClassToHandle()
                    .isAssignableFrom(classToHandle)) {
                returnValue.add(processor);
            }
        }

        if (cachedModelPostProcessorFactory != null) {
            returnValue.addAll(createIndividualPostProcessorList(classToHandle));
        }

        return returnValue;
    }

    private List<ModelPostProcessor> createIndividualPostProcessorList(final Class<?> classToHandle) {
        if (!classToHandle.getName()
                .startsWith(contextPath)) {
            return Collections.emptyList();
        }
        final List<ModelPostProcessor> returnValue = new LinkedList<>();

        returnValue.add(cachedModelPostProcessorFactory.get(classToHandle));

        if (classToHandle.getSuperclass() != null) {
            returnValue.addAll(createIndividualPostProcessorList(classToHandle.getSuperclass()));
        }

        return returnValue;
    }

}
