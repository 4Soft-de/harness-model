package com.foursoft.harness.kbl2vec.core;

import com.foursoft.harness.kbl2vec.convert.ConverterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransformationContextImpl implements TransformationContext {
    public static final String TRANSFORM_LOGGER = "com.foursoft.harness.kbl2vec.TRANSFORMATION_LOG";
    private final ConversionProperties conversionProperties;
    private final ConverterRegistry converterRegistry;
    private final EntityMapping entityMapping;
    private final Logger logger = LoggerFactory.getLogger(TRANSFORM_LOGGER);

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

    @Override public Logger getLogger() {
        return logger;
    }
}
