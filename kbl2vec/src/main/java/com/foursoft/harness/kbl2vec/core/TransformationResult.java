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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public record TransformationResult<D>(D element, List<Transformation<?, ?>> downstreamTransformations,
                                      List<Finalizer> finalizer) {

    public TransformationResult {

        Objects.requireNonNull(downstreamTransformations,
                               "downstreamTransformations must not be null, use empty list instead.");
        Objects.requireNonNull(finalizer, "callbacks must not be null, use empty list instead");
    }

    public boolean isEmpty() {
        return this.element == null;
    }

    public static <D> TransformationResult<D> noResult() {
        return new TransformationResult<>(null, Collections.emptyList(), Collections.emptyList());
    }

    public static <D> TransformationResult<D> of(final D element) {
        Objects.requireNonNull(element, "element must not be null");
        return new TransformationResult<>(element, Collections.emptyList(), Collections.emptyList());
    }

    public static <D> Builder<D> from(final D element) {
        return new Builder<>(element);
    }

    public static class Builder<D> {

        private final D element;
        private final List<Transformation<?, ?>> downstreamTransformations = new ArrayList<>();
        private final List<Finalizer> finalizers = new ArrayList<>();

        private Builder(final D element) {
            Objects.requireNonNull(element, "element must not be null");
            this.element = element;
        }

        public TransformationResult<D> build() {
            return new TransformationResult<>(element, List.copyOf(downstreamTransformations), List.copyOf(finalizers));
        }

        public <FROM, TO> Builder<D> downstreamTransformation(final Class<FROM> sourceClass,
                                                              final Class<TO> destinationClass,
                                                              final Query<FROM> sourceQuery,
                                                              final Supplier<List<? super TO>> contextList) {
            downstreamTransformations.add(new Transformation<>(sourceClass, destinationClass, sourceQuery,
                                                               (value) -> contextList.get()
                                                                       .add(value)));

            return this;
        }

        public <FROM, TO> Builder<D> withLinker(final Query<FROM> sourceObject, final Class<TO> targetClass,
                                                final Consumer<TO> targetProperty) {
            this.finalizers.add(new LinkingFinalizer<>(sourceObject, targetClass, targetProperty));
            return this;
        }

        public <FROM, TO> Builder<D> withLinker(final Query<FROM> sourceObject, final Class<TO> targetClass,
                                                final Supplier<List<TO>> targetProperty) {
            this.finalizers.add(new LinkingFinalizer<>(sourceObject, targetClass, targetProperty));
            return this;
        }

        public <FROM, TO> Builder<D> withLinker(final FROM sourceObject, final Class<TO> targetClass,
                                                final Supplier<List<TO>> targetProperty) {
            this.finalizers.add(new LinkingFinalizer<>(sourceObject, targetClass, targetProperty));
            return this;
        }

        public Builder<D> withFinalizer(final Finalizer finalizer) {
            finalizers.add(finalizer);
            return this;
        }

    }
}
