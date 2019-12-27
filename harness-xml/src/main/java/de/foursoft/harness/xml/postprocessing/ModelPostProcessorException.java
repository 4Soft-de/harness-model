package de.foursoft.harness.xml.postprocessing;

public class ModelPostProcessorException extends RuntimeException {

    private static final long serialVersionUID = -2319340169335393345L;

    public ModelPostProcessorException() {
        super();
    }

    public ModelPostProcessorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ModelPostProcessorException(final String message) {
        super(message);
    }

    public ModelPostProcessorException(final Throwable cause) {
        super(cause);
    }

}
