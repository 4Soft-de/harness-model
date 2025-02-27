package com.foursoft.harness.kbl2vec.core;

import com.foursoft.harness.vec.v2x.VecLanguageCode;

public class ConversionProperties {
    private VecLanguageCode defaultLanguageCode = VecLanguageCode.DE;

    public VecLanguageCode getDefaultLanguageCode() {
        return defaultLanguageCode;
    }

    public void setDefaultLanguageCode(final VecLanguageCode defaultLanguageCode) {
        this.defaultLanguageCode = defaultLanguageCode;
    }
}
