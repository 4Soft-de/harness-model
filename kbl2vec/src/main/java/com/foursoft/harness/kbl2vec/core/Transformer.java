package com.foursoft.harness.kbl2vec.core;

public interface Transformer<S, D> {

    TransformationResult<D> transform(S source);
}
