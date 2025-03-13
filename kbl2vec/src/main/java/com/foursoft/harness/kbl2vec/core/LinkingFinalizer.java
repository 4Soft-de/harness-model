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

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LinkingFinalizer<S, D> implements Finalizer {

    private final Query<S> sourceObjects;
    private final Class<D> targetClass;
    private final Consumer<D> targetProperty;

    public LinkingFinalizer(final Query<S> sourceObjects, final Class<D> targetClass,
                            final Consumer<D> targetProperty) {
        this.sourceObjects = sourceObjects;
        this.targetClass = targetClass;
        this.targetProperty = targetProperty;
    }

    public LinkingFinalizer(final Query<S> sourceObjects, final Class<D> targetClass,
                            final Supplier<List<? super D>> targetProperty) {
        this(sourceObjects, targetClass, value -> targetProperty.get()
                .add(value));
    }

    public LinkingFinalizer(final S sourceObject, final Class<D> targetClass,
                            final Supplier<List<? super D>> targetProperty) {
        this(Query.of(sourceObject), targetClass, value -> targetProperty.get()
                .add(value));
    }

    @Override
    public void finishTransformation(final TransformationContext context) {
        sourceObjects.stream()
                .map(s -> context.getEntityMapping()
                        .getIfUniqueOrElseThrow(s, targetClass))
                .forEach(targetProperty);
    }
}
