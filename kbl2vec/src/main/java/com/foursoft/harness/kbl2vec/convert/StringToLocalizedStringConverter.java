package com.foursoft.harness.kbl2vec.convert;

import com.foursoft.harness.vec.v2x.VecLanguageCode;
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.google.common.base.Strings;

import java.util.Optional;

public class StringToLocalizedStringConverter implements Converter<String, Optional<VecLocalizedString>> {

    private final VecLanguageCode defaultLanguage;

    public StringToLocalizedStringConverter(final VecLanguageCode defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    @Override
    public Optional<VecLocalizedString> convert(final String source) {
        if (Strings.isNullOrEmpty(source)) {
            return Optional.empty();
        }
        final VecLocalizedString localizedString = new VecLocalizedString();
        localizedString.setValue(source);
        localizedString.setLanguageCode(defaultLanguage);
        return Optional.of(localizedString);

    }
}
