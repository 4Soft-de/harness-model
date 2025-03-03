package com.foursoft.harness.kbl2vec.core;

@FunctionalInterface
public interface TransformationFragment<D> {

    void performFragment(D resultValue, TransformationResult.Builder<D> builder);

}
