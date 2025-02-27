package com.foursoft.harness.kbl2vec.core;

public interface TransformerRegistry {

    <S, D> Transformer<S, D> getTransformer(Class<S> source, Class<D> destination);
}
