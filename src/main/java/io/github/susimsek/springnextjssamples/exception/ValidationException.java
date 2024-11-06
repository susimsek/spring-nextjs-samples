package io.github.susimsek.springnextjssamples.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a validation error occurs in the application.
 * <p>
 * This exception extends {@link LocalizedException} and is typically used to indicate
 * that a request failed validation. It sets the HTTP status code to {@link HttpStatus#BAD_REQUEST} (400 Bad Request).
 * </p>
 */
public class ValidationException extends LocalizedException {

    /**
     * Constructs a new {@code ValidationException} with the specified validation message.
     *
     * @param message the error message describing the validation failure, typically a key for localization
     */
    public ValidationException(String message) {
        super(ErrorCode.INVALID_REQUEST.errorCode(), message, HttpStatus.BAD_REQUEST);
    }
}
