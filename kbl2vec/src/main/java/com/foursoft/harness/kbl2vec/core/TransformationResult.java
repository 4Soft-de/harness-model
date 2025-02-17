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

    public static <D> TransformationResult<D> of(D element) {
        Objects.requireNonNull(element, "element must not be null");
        return new TransformationResult<>(element, Collections.emptyList(), Collections.emptyList());
    }

    public static <D> Builder<D> from(D element) {
        return new Builder<>(element);
    }

    public static class Builder<D> {

        private final D element;
        private final List<Transformation<?, ?>> downstreamTransformations = new ArrayList<>();
        private final List<Finalizer> finalizers = new ArrayList<>();

        private Builder(D element) {
            Objects.requireNonNull(element, "element must not be null");
            this.element = element;
        }

        public TransformationResult<D> build() {
            return new TransformationResult<>(element, List.copyOf(downstreamTransformations), List.copyOf(finalizers));
        }

        public <FROM, TO> Builder<D> downstreamTransformation(Class<FROM> sourceClass, Class<TO> destinationClass,
                Query<FROM> sourceQuery, Supplier<List<? super TO>> contextList) {
            downstreamTransformations.add(new Transformation<>(sourceClass, destinationClass, sourceQuery,
                    (value) -> contextList.get()
                            .addLast(value)));

            return this;
        }

        public <FROM, TO> Builder<D> withLinker(Query<FROM> sourceObject, Class<TO> targetClass,
                Consumer<TO> targetProperty) {
            this.finalizers.add(new LinkingFinalizer<>(sourceObject, targetClass, targetProperty));
            return this;
        }

        public <FROM, TO> Builder<D> withLinker(Query<FROM> sourceObject, Class<TO> targetClass,
                Supplier<List<TO>> targetProperty) {
            this.finalizers.add(new LinkingFinalizer<>(sourceObject, targetClass, targetProperty));
            return this;
        }

        public <FROM, TO> Builder<D> withLinker(FROM sourceObject, Class<TO> targetClass,
                Supplier<List<TO>> targetProperty) {
            this.finalizers.add(new LinkingFinalizer<>(sourceObject, targetClass, targetProperty));
            return this;
        }

        public Builder<D> withFinalizer(Finalizer finalizer) {
            finalizers.add(finalizer);
            return this;
        }

    }
}
