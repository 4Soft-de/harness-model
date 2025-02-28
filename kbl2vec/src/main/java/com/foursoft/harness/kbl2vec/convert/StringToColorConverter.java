package com.foursoft.harness.kbl2vec.convert;

import com.foursoft.harness.vec.v2x.VecColor;
import com.google.common.base.Strings;

import java.util.Optional;

public class StringToColorConverter implements Converter<String, Optional<VecColor>> {

    private final String referenceSystem;

    public StringToColorConverter(final String referenceSystem) {
        this.referenceSystem = referenceSystem;
    }

    @Override
    public Optional<VecColor> convert(final String source) {
        if (Strings.isNullOrEmpty(source)) {
            return Optional.empty();
        }
        final VecColor vecColor = new VecColor();
        vecColor.setKey(source);
        vecColor.setReferenceSystem(referenceSystem);
        return Optional.of(vecColor);
    }
}
