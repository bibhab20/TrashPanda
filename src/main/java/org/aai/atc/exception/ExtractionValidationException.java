package org.aai.atc.exception;

public class ExtractionValidationException extends Exception {

    public ExtractionValidationException() {
    }

    public ExtractionValidationException(String message) {
        super(message);
    }

    public ExtractionValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtractionValidationException(Throwable cause) {
        super(cause);
    }

    public ExtractionValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
