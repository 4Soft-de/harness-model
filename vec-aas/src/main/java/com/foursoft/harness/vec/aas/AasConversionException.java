package com.foursoft.harness.vec.aas;

public class AasConversionException extends RuntimeException {
    public AasConversionException() {
    }

    public AasConversionException(final String message) {
        super(message);
    }

    public AasConversionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
