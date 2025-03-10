package com.foursoft.harness.kbl2vec.core;

@FunctionalInterface
public interface TransformationFragment<D, B> {

    void performFragment(D resultValue, B builder);

}
