package com.foursoft.harness.kbl2vec.factories;

import com.foursoft.harness.kbl2vec.core.ConversionProperties;

public class Factories {

    private final ConversionProperties properties;
    private final LocalizedStringFactory localizedStringFactory;

    public Factories(final ConversionProperties properties) {
        this.properties = properties;
        localizedStringFactory = new LocalizedStringFactory(properties);
    }

    public LocalizedStringFactory getLocalizedStringFactory() {
        return localizedStringFactory;
    }
}
