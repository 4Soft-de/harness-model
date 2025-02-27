package com.foursoft.harness.kbl2vec.core;

import com.foursoft.harness.kbl2vec.convert.ConverterRegistry;

public class TransformationContextImpl implements TransformationContext {
    private final ConversionProperties conversionProperties;
    private final ConverterRegistry converterRegistry;
    private final EntityMapping entityMapping;

    public TransformationContextImpl(final ConversionProperties conversionProperties,
                                     final ConverterRegistry converterRegistry, final EntityMapping entityMapping) {
        this.conversionProperties = conversionProperties;
        this.converterRegistry = converterRegistry;
        this.entityMapping = entityMapping;
    }

    @Override
    public ConverterRegistry getConverterRegistry() {
        return converterRegistry;
    }

    @Override
    public ConversionProperties getConversionProperties() {
        return conversionProperties;
    }

    @Override
    public EntityMapping getEntityMapping() {
        return entityMapping;
    }
}
