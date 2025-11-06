package com.foursoft.harness.navext.runtime.exception;

/**
 * Exception which is thrown if the validation of an XML-based file fails.
 */
public class XmlValidationException extends RuntimeException {

    public XmlValidationException(final String message) {
        super(message);
    }

    public XmlValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public XmlValidationException(final Throwable cause) {
        super(cause);
    }

}
