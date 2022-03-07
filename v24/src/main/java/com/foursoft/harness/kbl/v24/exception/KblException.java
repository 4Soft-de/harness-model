package com.foursoft.harness.kbl.v24.exception;

public class KblException extends RuntimeException {

    public KblException(String message) {
        super(message);
    }

    public KblException(String message, Throwable cause) {
        super(message, cause);
    }

    public KblException(Throwable cause) {
        super(cause);
    }
}
