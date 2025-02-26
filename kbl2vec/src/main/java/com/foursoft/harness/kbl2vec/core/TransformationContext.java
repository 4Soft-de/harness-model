package com.foursoft.harness.kbl2vec.core;

import com.foursoft.harness.kbl2vec.convert.ConverterRegistry;

public interface TransformationContext {

    EntityMapping getEntityMapping();

    ConversionProperties getConversionProperties();

    ConverterRegistry getConverterRegistry();

}
