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

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multimaps;

import java.util.List;
import java.util.Objects;

public class EntityMapping {

    Multimap<Object, Object> mappedElements = MultimapBuilder.hashKeys()
            .arrayListValues()
            .build();

    public void put(final Object sourceEntity, final Object destinationEntity) {
        Objects.requireNonNull(sourceEntity, "sourceEntity must not be null");
        Objects.requireNonNull(destinationEntity, "destinationEntity must not be null");
        mappedElements.put(sourceEntity, destinationEntity);

    }

    public Multimap<Object, Object> getContent() {
        return Multimaps.unmodifiableMultimap(mappedElements);
    }

    /**
     * Returns an element from the cache, matching the {@code destinationClass} criteria.
     * <p>
     * If nothing is found, or if more than one element is found, the method throws an exception.
     */
    public <D> D getIfUniqueOrElseThrow(final Object sourceEntity, final Class<D> destinationClass) {

        final List<D> result = mappedElements.get(sourceEntity)
                .stream()
                .filter(destinationClass::isInstance)
                .map(destinationClass::cast)
                .toList();

        if (result.isEmpty()) {
            throw new ConversionException(
                    String.format("No transformation result found for source: '%1s' and destination type: '%2s'",
                                  sourceEntity, destinationClass.getSimpleName()));
        }
        if (result.size() > 1) {
            throw new ConversionException(String.format(
                    "Expected exactly one transformation result for source: '%1s' and destination type: '%2s', but " +
                            "found: %3s",
                    sourceEntity, destinationClass.getSimpleName(), result.size()));
        }

        return result.get(0);
    }

}
