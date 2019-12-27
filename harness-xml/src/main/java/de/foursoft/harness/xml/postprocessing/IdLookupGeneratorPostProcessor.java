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
package de.foursoft.harness.xml.postprocessing;

import java.util.HashMap;
import java.util.function.Function;

import de.foursoft.harness.xml.IdLookupProvider;

/**
 * {@link ModelPostProcessor} to generate a {@link IdLookupProvider} during the
 * unmarshalling Process.
 * 
 * @author becker
 *
 * @param <I>
 *            Type of the identifiable elements.
 */
public class IdLookupGeneratorPostProcessor<I> implements ModelPostProcessor {

    private final Class<I> identifiableElements;
    private final Function<I, String> idMapper;

    private int objectCount = 0;
    private HashMap<String, I> idLookup;

    public IdLookupGeneratorPostProcessor(final Class<I> identifiableElements, final Function<I, String> idMapper) {
        this.identifiableElements = identifiableElements;
        this.idMapper = idMapper;
    }

    @Override
    public Class<?> getClassToHandle() {
        return identifiableElements;
    }

    @Override
    public void afterUnmarshalling(final Object target, final Object parent) {
        objectCount++;
    }

    @Override
    public void afterUnmarshallingCompleted(final Object target) {
        // No type check for performance reasons. The ModelPostProcessorRegistry
        // ensures that this handler is only called correctly.
        if (idLookup == null) {
            idLookup = new HashMap<>(objectCount);
        }
        final I element = identifiableElements.cast(target);
        idLookup.put(idMapper.apply(element), element);
    }

    @Override
    public void clearState() {
        objectCount = 0;
        idLookup = null;
    }

    /**
     * Creates a new {@link IdLookupProvider} based on the current state of the
     * post processor.
     * 
     * @return
     */
    public IdLookupProvider<I> createIdLookkup() {
        return new IdLookupProvider<>(idLookup);
    }

}
