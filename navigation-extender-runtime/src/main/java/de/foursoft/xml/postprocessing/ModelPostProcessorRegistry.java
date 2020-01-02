/*-
 * ========================LICENSE_START=================================
 * xml-runtime
 * %%
 * Copyright (C) 2019 4Soft GmbH
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
package de.foursoft.xml.postprocessing;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.foursoft.xml.cache.SimpleCache;

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
     *  Adds a default post processor. It will be used if no special postprocessor is specified.
     * @param postProcessor the post processor
     * @return this for fluent API
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
     * @param modelPostProcessorFactory a function providing a special post processor per class
     * @return this for fluent API
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
     * @param postProcessorType the class of the post processors to find
     * @param <T> the type of processors to look for
     * @return the list of processor of the given type. Can be empty - never null.
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
     * <p>
     * Applicable {@link ModelPostProcessor}s are basically all that satisfy the interface and can handle the given class.
     * 
     * <tt>{@link ModelPostProcessor#getClassToHandle()}.isAssignableFrom(classToHandle)</tt>.
     * 
     * @param classToHandle the class to processor
     * @return the list of post processors found for the given class.
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
