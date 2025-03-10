package com.foursoft.harness.kbl2vec.core;

import java.util.function.Consumer;

public record Transformation<S, D>(Class<S> sourceClass, Class<D> destinationClass, Query<S> sourceQuery,
                                   Consumer<D> accumulator) {

}
