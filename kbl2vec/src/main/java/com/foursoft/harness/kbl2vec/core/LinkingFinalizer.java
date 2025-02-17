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
                            final Supplier<List<D>> targetProperty) {
        this(sourceObjects, targetClass, (value) -> targetProperty.get()
                .addLast(value));
    }

    public LinkingFinalizer(final S sourceObject, final Class<D> targetClass, final Supplier<List<D>> targetProperty) {
        this(Query.of(sourceObject), targetClass, (value) -> targetProperty.get()
                .addLast(value));
    }

    @Override
    public void finalize(final TransformationContext context) {
        sourceObjects.stream()
                .map(s -> context.getEntityCache()
                        .getIfUniqueOrElseThrow(s, targetClass))
                .forEach(targetProperty);
    }
}
