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
package com.foursoft.xml;

import java.util.Map;
import java.util.Optional;

/**
 * Provides the possibility to lookup elements by their technical (xml) id. The
 * {@link IdLookupProvider} is normally created during the unmarshalling process
 * by an {@link ExtendedUnmarshaller}.
 * 
 * @author becker
 *
 * @param <I>
 *            Type of the superclass if elements that are identifiable (have an
 *            id).
 */
public class IdLookupProvider<I> {

    private final Map<String, I> idLookup;

    public IdLookupProvider(final Map<String, I> idLookup) {
        this.idLookup = idLookup;
    }

    public <T extends I> Optional<T> findById(final Class<T> clazz, final String id) {
        final I i = idLookup.get(id);
        return clazz.isInstance(i) ? (Optional<T>)Optional.of(i) : Optional.empty();
    }

    /**
     * Merges this {@link IdLookupProvider} with <tt>toMerge</tt> and returns
     * <tt>this</tt>. Duplicate id mappings are removed, the mappings in
     * <tt>toMerge</tt> are kept.
     * 
     * @param toMerge the id provider to merge
     * @return this for fluent API
     */
    public IdLookupProvider<I> merge(final IdLookupProvider<I> toMerge) {
        idLookup.putAll(toMerge.idLookup);
        return this;
    }

}
