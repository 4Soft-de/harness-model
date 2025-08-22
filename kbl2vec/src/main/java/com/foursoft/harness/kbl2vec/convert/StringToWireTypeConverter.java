package com.foursoft.harness.kbl2vec.convert;

import com.foursoft.harness.vec.v2x.VecWireType;
import com.google.common.base.Strings;

import java.util.Optional;

public class StringToWireTypeConverter implements Converter<String, Optional<VecWireType>> {

    private final String referenceSystem;

    public StringToWireTypeConverter(final String referenceSystem) {
        this.referenceSystem = referenceSystem;
    }

    @Override
    public Optional<VecWireType> convert(final String source) {
        if (Strings.isNullOrEmpty(source)) {
            return Optional.empty();
        }
        final VecWireType wireType = new VecWireType();
        wireType.setReferenceSystem(referenceSystem);
        wireType.setType(source);
        return Optional.of(wireType);
    }
}
