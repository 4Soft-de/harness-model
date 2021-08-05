package com.foursoft.vecmodel.common.exception;

public class VecException extends RuntimeException {

    private static final long serialVersionUID = -2971175765070712256L;

    public VecException(final String message) {
        super(message);
    }

    public VecException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public VecException(final Throwable cause) {
        super(cause);
    }

}