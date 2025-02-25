package com.foursoft.harness.kbl2vec.core;

/**
 * A Transformer defines the first step of the two phase conversion. It also registers the linker for the second
 * phase. A
 * <p>
 * transformer is used to transform complex objects that have incoming or outgoing relations.
 *
 * @param <S>
 * @param <D>
 */
public interface Transformer<S, D> {

    TransformationResult<D> transform(S source);
}
