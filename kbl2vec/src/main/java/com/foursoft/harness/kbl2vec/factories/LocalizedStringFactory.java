package com.foursoft.harness.kbl2vec.factories;

import com.foursoft.harness.kbl2vec.core.ConversionProperties;
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.google.common.base.Strings;

import java.util.Optional;

public class LocalizedStringFactory {

    private final ConversionProperties properties;

    public LocalizedStringFactory(final ConversionProperties properties) {
        this.properties = properties;
    }

    public Optional<VecLocalizedString> toLocalizedString(final String value) {
        if (Strings.isNullOrEmpty(value)) {
            return Optional.empty();
        }
        final VecLocalizedString localizedString = new VecLocalizedString();
        localizedString.setValue(value);
        localizedString.setLanguageCode(properties.getDefaultLanguageCode());
        return Optional.of(localizedString);
    }
}
