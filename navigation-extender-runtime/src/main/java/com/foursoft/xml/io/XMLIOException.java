package com.foursoft.xml.io;

public class XMLIOException extends RuntimeException {

    private static final long serialVersionUID = -3029716128777857369L;

    public XMLIOException() {
        super();
    }

    public XMLIOException(final String message, final Throwable cause, final boolean enableSuppression,
                          final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public XMLIOException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public XMLIOException(final String message) {
        super(message);
    }

    public XMLIOException(final Throwable cause) {
        super(cause);
    }

}
