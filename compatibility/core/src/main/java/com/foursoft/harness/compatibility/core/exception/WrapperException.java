package com.foursoft.harness.compatibility.core.exception;

/**
 * Exception class for wrapping-related errors.
 */
public class WrapperException extends RuntimeException {

    private static final long serialVersionUID = -2613188879924314781L;

    public WrapperException(final String message) {
        super(message);
    }

    public WrapperException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public WrapperException(final Throwable cause) {
        super(cause);
    }

}
