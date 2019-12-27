package de.foursoft.harness.xml;

public class JaxbModelException extends RuntimeException {

    private static final long serialVersionUID = 4813744866871637902L;

    public JaxbModelException() {
        super();
    }

    public JaxbModelException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public JaxbModelException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public JaxbModelException(final String message) {
        super(message);
    }

    public JaxbModelException(final Throwable cause) {
        super(cause);
    }

}
