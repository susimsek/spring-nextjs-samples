package io.github.susimsek.springnextjssamples.exception;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

/**
 * Enum representing a set of standardized error codes, HTTP statuses, and message keys
 * used across the application for consistent error handling and messaging.
 * <p>
 * Each constant in this enum corresponds to a specific error scenario, with an associated
 * error code, HTTP status, and message key for localization.
 * </p>
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum ErrorCode {

    /** Invalid request error with HTTP status 400 Bad Request. */
    INVALID_REQUEST("invalid_request", HttpStatus.BAD_REQUEST, "error.invalid_request"),

    /** Access denied error with HTTP status 403 Forbidden. */
    ACCESS_DENIED("access_denied", HttpStatus.FORBIDDEN, "error.access_denied"),

    /** Unsupported response type error with HTTP status 400 Bad Request. */
    UNSUPPORTED_RESPONSE_TYPE("unsupported_response_type", HttpStatus.BAD_REQUEST,
        "error.unsupported_response_type"),

    /** Invalid token error with HTTP status 401 Unauthorized. */
    INVALID_TOKEN("invalid_token", HttpStatus.UNAUTHORIZED, "error.invalid_token"),

    /** Invalid credentials error with HTTP status 401 Unauthorized. */
    INVALID_CREDENTIALS("invalid_credentials", HttpStatus.UNAUTHORIZED, "error.invalid_credentials"),

    /** Server error with HTTP status 500 Internal Server Error. */
    SERVER_ERROR("server_error", HttpStatus.INTERNAL_SERVER_ERROR, "error.server_error"),

    /** Temporarily unavailable error with HTTP status 503 Service Unavailable. */
    TEMPORARILY_UNAVAILABLE("temporarily_unavailable", HttpStatus.SERVICE_UNAVAILABLE,
        "error.temporarily_unavailable"),

    /** Not acceptable error with HTTP status 406 Not Acceptable. */
    NOT_ACCEPTABLE("not_acceptable", HttpStatus.NOT_ACCEPTABLE, "error.not_acceptable"),

    /** Unsupported media type error with HTTP status 415 Unsupported Media Type. */
    UNSUPPORTED_MEDIA_TYPE("unsupported_media_type", HttpStatus.UNSUPPORTED_MEDIA_TYPE,
        "error.unsupported_media_type"),

    /** Method not allowed error with HTTP status 405 Method Not Allowed. */
    METHOD_NOT_ALLOWED("method_not_allowed", HttpStatus.METHOD_NOT_ALLOWED, "error.method_not_allowed"),

    /** Message not readable error with HTTP status 400 Bad Request. */
    MESSAGE_NOT_READABLE("invalid_request", HttpStatus.BAD_REQUEST, "error.message_not_readable"),

    /** Type mismatch error with HTTP status 400 Bad Request. */
    TYPE_MISMATCH("invalid_request", HttpStatus.BAD_REQUEST, "error.type_mismatch"),

    /** Missing parameter error with HTTP status 400 Bad Request. */
    MISSING_PARAMETER("invalid_request", HttpStatus.BAD_REQUEST, "error.missing_parameter"),

    /** Missing part error with HTTP status 400 Bad Request. */
    MISSING_PART("invalid_request", HttpStatus.BAD_REQUEST, "error.missing_part"),

    /** Request binding error with HTTP status 400 Bad Request. */
    REQUEST_BINDING("invalid_request", HttpStatus.BAD_REQUEST, "error.request_binding"),

    /** Multipart request error with HTTP status 400 Bad Request. */
    MULTIPART("invalid_request", HttpStatus.BAD_REQUEST, "error.multipart"),

    /** Payload too large error with HTTP status 413 Payload Too Large. */
    PAYLOAD_TOO_LARGE("payload_too_large", HttpStatus.PAYLOAD_TOO_LARGE, "error.payload_too_large"),

    /** Validation failed error with HTTP status 400 Bad Request. */
    VALIDATION_FAILED("invalid_request", HttpStatus.BAD_REQUEST, "error.validation_failed"),

    /** Resource not found error with HTTP status 404 Not Found. */
    NOT_FOUND("resource_not_found", HttpStatus.NOT_FOUND, "error.not_found"),

    /** Unsupported operation error with HTTP status 501 Not Implemented. */
    UNSUPPORTED_OPERATION("unsupported_operation", HttpStatus.NOT_IMPLEMENTED, "error.unsupported_operation"),

    /** Gateway timeout error with HTTP status 504 Gateway Timeout. */
    GATEWAY_TIMEOUT("gateway_timeout", HttpStatus.GATEWAY_TIMEOUT, "error.gateway_timeout"),

    /** Rate limit exceeded error with HTTP status 429 Too Many Requests. */
    RATE_LIMIT_EXCEEDED("rate_limit_exceeded", HttpStatus.TOO_MANY_REQUESTS, "error.rate_limit_exceeded"),

    /** Unsupported API version error with HTTP status 504 Gateway Timeout. */
    UNSUPPORTED_API_VERSION("unsupported_api_version", HttpStatus.GATEWAY_TIMEOUT,
        "error.unsupported_api_version");

    /** The unique error code representing the error scenario. */
    private final String errorCode;

    /** The HTTP status associated with this error code. */
    private final HttpStatus httpStatus;

    /** The message key for localization of the error message. */
    private final String messageKey;

    /**
     * Retrieves the {@code ErrorCode} corresponding to the specified error code string.
     *
     * @param errorCode the error code string to look up
     * @return an {@code Optional} containing the matching {@code ErrorCode}, or empty if not found
     */
    public static Optional<ErrorCode> fromErrorCode(String errorCode) {
        return Arrays.stream(values())
            .filter(code -> code.errorCode().equals(errorCode))
            .findFirst();
    }
}
