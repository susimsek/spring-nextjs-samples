package io.github.susimsek.springnextjssamples.exception.versioning;

/**
 * Exception thrown when an unsupported API version is requested.
 * <p>
 * This exception extends {@link RuntimeException} and is typically used to indicate that
 * the requested API version is not supported by the application.
 * </p>
 */
public class UnsupportedApiVersionException extends RuntimeException {

    /**
     * Constructs a new {@code UnsupportedApiVersionException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public UnsupportedApiVersionException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code UnsupportedApiVersionException} with the specified detail message
     * and cause.
     *
     * @param message the detail message explaining the reason for the exception
     * @param cause the cause of the exception, used for exception chaining
     */
    public UnsupportedApiVersionException(String message, Throwable cause) {
        super(message, cause);
    }
}
