package com.foursoft.harness.kbl2vec.core;

public class NoMappingDefinedException extends ConversionException {
    public NoMappingDefinedException(final String message) {
        super(message);
    }

    public NoMappingDefinedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
