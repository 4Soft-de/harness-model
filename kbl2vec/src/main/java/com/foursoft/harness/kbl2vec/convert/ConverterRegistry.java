package com.foursoft.harness.kbl2vec.convert;

import com.foursoft.harness.kbl2vec.core.ConversionProperties;
import com.foursoft.harness.vec.v2x.VecLocalizedString;

import java.util.Objects;
import java.util.Optional;

public class ConverterRegistry {

    private final Converter<String, Optional<VecLocalizedString>> stringToLocalizedString;

    public ConverterRegistry(final ConversionProperties conversionProperties) {
        Objects.requireNonNull(conversionProperties);
        this.stringToLocalizedString = new StringToLocalizedStringConverter(conversionProperties
                                                                                    .getDefaultLanguageCode());
    }

    public Converter<String, Optional<VecLocalizedString>> getStringToLocalizedString() {
        return stringToLocalizedString;
    }
}
