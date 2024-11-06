package io.github.susimsek.springnextjssamples.exception;

import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

/**
 * A custom exception class for handling localized exceptions in the application.
 * <p>
 * This class extends {@link RuntimeException} and provides additional fields for
 * managing error codes, HTTP status codes, and message arguments for localization.
 * </p>
 * <p>
 * The `LocalizedException` class allows the inclusion of named arguments or positional arguments
 * to provide more context for localized error messages.
 * </p>
 */
@Getter
public class LocalizedException extends RuntimeException {

    /** The unique error code associated with this exception. */
    private final String errorCode;

    /** An array of positional arguments for localization purposes. */
    private final transient Object[] args;

    /** A map of named arguments for localization purposes. */
    private final Map<String, Object> namedArgs;

    /** The HTTP status code to return in the response for this exception. */
    private final HttpStatusCode status;

    /**
     * Constructs a new {@code LocalizedException} with the specified error code,
     * message, and HTTP status.
     *
     * @param errorCode the unique error code associated with this exception
     * @param message the error message, typically a key for localization
     * @param status the HTTP status code to be returned in the response
     */
    public LocalizedException(String errorCode,
                              String message, HttpStatusCode status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
        this.args = null;
        this.namedArgs = null;
    }

    /**
     * Constructs a new {@code LocalizedException} with the specified error code,
     * message, HTTP status, and positional arguments for localization.
     *
     * @param errorCode the unique error code associated with this exception
     * @param message the error message, typically a key for localization
     * @param status the HTTP status code to be returned in the response
     * @param args an array of positional arguments for message localization
     */
    public LocalizedException(String errorCode,
                              String message,
                              HttpStatusCode status,
                              Object... args) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
        this.args = args;
        this.namedArgs = null;
    }

    /**
     * Constructs a new {@code LocalizedException} with the specified error code,
     * message, HTTP status, and named arguments for localization.
     *
     * @param errorCode the unique error code associated with this exception
     * @param message the error message, typically a key for localization
     * @param status the HTTP status code to be returned in the response
     * @param namedArgs a map of named arguments for message localization
     */
    public LocalizedException(String errorCode, String message,
                              HttpStatusCode status,
                              Map<String, Object> namedArgs) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
        this.args = null;
        this.namedArgs = namedArgs;
    }
}
