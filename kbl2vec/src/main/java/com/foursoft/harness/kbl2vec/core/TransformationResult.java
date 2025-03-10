package com.foursoft.harness.kbl2vec.core;

import com.foursoft.harness.navext.runtime.model.Identifiable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public record TransformationResult<D>(D element, List<Transformation<?, ?>> downstreamTransformations,
                                      List<Finalizer> finalizer, Map<Object, String> comments) {

    public TransformationResult {

        Objects.requireNonNull(downstreamTransformations,
                               "downstreamTransformations must not be null, use empty list instead.");
        Objects.requireNonNull(finalizer, "callbacks must not be null, use empty list instead");
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
        private final List<Finalizer> finalizers = new ArrayList<>();
        private final Map<Object, String> comments = new HashMap<>();

        private Builder(final D element) {
            Objects.requireNonNull(element, "element must not be null");
            this.element = element;
        }

        public TransformationResult<D> build() {
            return new TransformationResult<>(element, List.copyOf(downstreamTransformations), List.copyOf(finalizers),
                                              comments);
        }

        public <FROM, TO> Builder<D> downstreamTransformation(final Class<FROM> sourceClass,
                                                              final Class<TO> destinationClass,
                                                              final Query<FROM> sourceQuery,
                                                              final Consumer<TO> accumulator) {
            downstreamTransformations.add(
                    new Transformation<>(sourceClass, destinationClass, sourceQuery, accumulator));

            return this;
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

        public Builder<D> withComment(final String comment) {
            this.comments.merge(element, comment, (a, b) -> a + "\n" + b);

            return this;
        }

        public Builder<D> withCommentOnDetail(final Identifiable detail, final String comment) {
            this.comments.merge(detail, comment, (a, b) -> a + "\n" + b);
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

        public Builder<D> withFragment(final TransformationFragment<D, Builder<D>> fragment) {
            fragment.performFragment(element, this);
            return this;
        }

        public Builder<D> withFinalizer(final Finalizer finalizer) {
            finalizers.add(finalizer);
            return this;
        }
    }

}
