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

import com.foursoft.harness.navext.runtime.model.Identifiable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public record TransformationResult<D>(D element, List<Transformation<?, ?>> downstreamTransformations,
                                      List<Finisher> finisher, Map<Object, String> comments) {

    public TransformationResult {

        Objects.requireNonNull(downstreamTransformations,
                               "downstreamTransformations must not be null, use empty list instead.");
        Objects.requireNonNull(finisher, "callbacks must not be null, use empty list instead");
    }

    public boolean isEmpty() {
        return this.element == null;
    }

    public static <D> TransformationResult<D> noResult() {
        return new TransformationResult<>(null, Collections.emptyList(), Collections.emptyList(),
                                          Collections.emptyMap());
    }

    public static <D> TransformationResult<D> of(final D element) {
        Objects.requireNonNull(element, "element must not be null");
        return new TransformationResult<>(element, Collections.emptyList(), Collections.emptyList(),
                                          Collections.emptyMap());
    }

    public static <D> Builder<D> from(final D element) {
        return new Builder<>(element);
    }

    public static class Builder<D> {

        private final D element;
        private final List<Transformation<?, ?>> downstreamTransformations = new ArrayList<>();
        private final List<Finisher> finishers = new ArrayList<>();
        private final Map<Object, String> comments = new HashMap<>();

        private Builder(final D element) {
            Objects.requireNonNull(element, "element must not be null");
            this.element = element;
        }

        public TransformationResult<D> build() {
            return new TransformationResult<>(element, List.copyOf(downstreamTransformations), List.copyOf(finishers),
                                              comments);
        }

        public <FROM, TO> Builder<D> withDownstream(final Class<FROM> sourceClass,
                                                    final Class<TO> destinationClass,
                                                    final Query<FROM> sourceQuery,
                                                    final BiConsumer<D, TO> accumulator) {
            downstreamTransformations.add(
                    new Transformation<>(sourceClass, destinationClass, sourceQuery,
                                         result -> accumulator.accept(element, result)));

            return this;
        }

        public <FROM, TO> Builder<D> withDownstream(final Class<FROM> sourceClass,
                                                    final Class<TO> destinationClass,
                                                    final Query<FROM> sourceQuery,
                                                    final Function<D, List<? super TO>> contextListProvider) {
            downstreamTransformations.add(new Transformation<>(sourceClass, destinationClass, sourceQuery,
                                                               value -> contextListProvider.apply(element).add(value)));

            return this;
        }

        public Builder<D> withComment(final String comment) {
            this.comments.merge(element, comment, (a, b) -> a + "\n" + b);

            return this;
        }

        public Builder<D> withCommentOnDetail(final Identifiable detail, final String comment) {
            this.comments.merge(detail, comment, (a, b) -> a + "\n" + b);
            return this;
        }

        public <FROM, TO> Builder<D> withLinker(final Query<FROM> sourceObject, final Class<TO> targetClass,
                                                final BiConsumer<D, TO> linker) {
            this.finishers.add(
                    new LinkingFinisher<>(sourceObject, targetClass, target -> linker.accept(element, target)));
            return this;
        }

        public <FROM, TO> Builder<D> withLinker(final Query<FROM> sourceObject, final Class<TO> targetClass,
                                                final Function<D, List<? super TO>> targetListMapper) {
            return this.withLinker(sourceObject, targetClass,
                                   (context, target) -> targetListMapper.apply(context).add(target));
        }

        public <FROM, TO> Builder<D> withLinker(final FROM sourceObject, final Class<TO> targetClass,
                                                final Function<D, List<? super TO>> targetListMapper) {
            return this.withLinker(Query.of(sourceObject), targetClass, targetListMapper);
        }

        public Builder<D> withFragment(final TransformationFragment<D, Builder<D>> fragment) {
            fragment.performFragment(element, this);
            return this;
        }

        public Builder<D> withFinisher(final Finisher finisher) {
            finishers.add(finisher);
            return this;
        }
    }

}
