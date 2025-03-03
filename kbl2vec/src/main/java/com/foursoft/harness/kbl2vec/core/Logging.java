package com.foursoft.harness.kbl2vec.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Logging {
    public static final String TRANSFORM_LOGGER_NAME = "com.foursoft.harness.kbl2vec.TRANSFORMATION_LOG";

    public static final Logger TRANSFORM_LOGGER = LoggerFactory.getLogger(TRANSFORM_LOGGER_NAME);

    private Logging() {
        throw new AssertionError("Do not instantiate!");
    }

}
