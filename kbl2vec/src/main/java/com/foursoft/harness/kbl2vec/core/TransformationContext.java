package com.foursoft.harness.kbl2vec.core;

import com.foursoft.harness.kbl2vec.convert.ConverterRegistry;
import org.slf4j.Logger;

public interface TransformationContext {

    EntityMapping getEntityMapping();

    ConversionProperties getConversionProperties();

    ConverterRegistry getConverterRegistry();

    Logger getLogger();

}
