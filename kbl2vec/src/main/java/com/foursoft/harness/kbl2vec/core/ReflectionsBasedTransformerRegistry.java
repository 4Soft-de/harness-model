package com.foursoft.harness.kbl2vec.core;

public class ReflectionsBasedTransformerRegistry implements TransformerRegistry {

    @Override
    public <S, D> Transformer<S, D> getTransformer(final Class<S> source, final Class<D> destination) {
        return null;
    }
}
