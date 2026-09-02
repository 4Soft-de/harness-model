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
import org.slf4j.Logger;

import java.util.function.Supplier;

public interface TransformationContext {

    EntityMapping getEntityMapping();

    ConversionProperties getConversionProperties();

    ConverterRegistry getConverterRegistry();

    Logger getLogger();

    int getNewId();

    /**
     * Returns a value derived from the source model, computing it with the given supplier on first access and
     * reusing it for every later call with an equal key.
     * <p>
     * This exists for lookups that would otherwise have to be rebuilt for every element that needs them, for
     * example an index over a collection of the source model. The cache lives as long as the conversion and is
     * intentionally untyped, so that the framework stays independent of the models being converted; the key
     * should identify both the source object and the kind of derived value.
     *
     * @param key      identifies the cached value, typically the source object the value is derived from
     * @param supplier computes the value if it is not cached yet
     * @return the cached value
     */
    <T> T getCached(Object key, Supplier<T> supplier);

}
