package com.foursoft.harness.kbl2vec.convert;

import com.foursoft.harness.kbl2vec.core.Transformer;

/**
 * Converter are utility components that convert a value or object into another one.
 * They are supposed to be used by {@link Transformer} for simple conversion tasks of objects that do
 * not play a significant role in the overall conversion process. Especially, converter shall only be used, when the
 * result of
 * conversion does not have to be referencable by other objects at a later stage. (e.g. converting a string into a
 * localized string).
 *
 * @param <S>
 * @param <D>
 */
public interface Converter<S, D> {

    D convert(final S source);
}
