package io.github.susimsek.springnextjssamples.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when user credentials are invalid during an authentication attempt.
 * <p>
 * This exception extends {@link LocalizedException} and provides a predefined error code
 * and message key for "invalid credentials" scenarios. The exception response is associated
 * with the HTTP status code 401 Unauthorized.
 * </p>
 */
public class InvalidCredentialsException extends LocalizedException {

    /**
     * Constructs a new {@code InvalidCredentialsException} with the error code and message key
     * defined by {@link ErrorCode#INVALID_CREDENTIALS}.
     * <p>
     * Sets the HTTP status to {@link HttpStatus#UNAUTHORIZED}.
     * </p>
     */
    public InvalidCredentialsException() {
        super(ErrorCode.INVALID_CREDENTIALS.errorCode(),
            ErrorCode.INVALID_CREDENTIALS.messageKey(), HttpStatus.UNAUTHORIZED);
    }
}
