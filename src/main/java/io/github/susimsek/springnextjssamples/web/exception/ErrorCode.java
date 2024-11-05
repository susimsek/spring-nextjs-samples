package io.github.susimsek.springnextjssamples.web.exception;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_REQUEST("invalid_request", HttpStatus.BAD_REQUEST, "error.invalid_request"),
    ACCESS_DENIED("access_denied", HttpStatus.FORBIDDEN, "error.access_denied"),
    UNSUPPORTED_RESPONSE_TYPE("unsupported_response_type", HttpStatus.BAD_REQUEST,
        "error.unsupported_response_type"),
    INVALID_TOKEN("invalid_token", HttpStatus.UNAUTHORIZED, "error.invalid_token"),
    INVALID_CREDENTIALS("invalid_credentials", HttpStatus.UNAUTHORIZED, "error.invalid_credentials"),
    SERVER_ERROR("server_error", HttpStatus.INTERNAL_SERVER_ERROR, "error.server_error"),
    TEMPORARILY_UNAVAILABLE("temporarily_unavailable", HttpStatus.SERVICE_UNAVAILABLE,
        "error.temporarily_unavailable"),
    NOT_ACCEPTABLE("not_acceptable", HttpStatus.NOT_ACCEPTABLE, "error.not_acceptable"),
    UNSUPPORTED_MEDIA_TYPE("unsupported_media_type", HttpStatus.UNSUPPORTED_MEDIA_TYPE,
        "error.unsupported_media_type"),
    METHOD_NOT_ALLOWED("method_not_allowed", HttpStatus.METHOD_NOT_ALLOWED, "error.method_not_allowed"),
    MESSAGE_NOT_READABLE("invalid_request", HttpStatus.BAD_REQUEST, "error.message_not_readable"),
    TYPE_MISMATCH("invalid_request", HttpStatus.BAD_REQUEST, "error.type_mismatch"),
    MISSING_PARAMETER("invalid_request", HttpStatus.BAD_REQUEST, "error.missing_parameter"),
    MISSING_PART("invalid_request", HttpStatus.BAD_REQUEST, "error.missing_part"),
    REQUEST_BINDING("invalid_request", HttpStatus.BAD_REQUEST, "error.request_binding"),
    MULTIPART("invalid_request", HttpStatus.BAD_REQUEST, "error.multipart"),
    PAYLOAD_TOO_LARGE("payload_too_large", HttpStatus.PAYLOAD_TOO_LARGE, "error.payload_too_large"),
    VALIDATION_FAILED("invalid_request", HttpStatus.BAD_REQUEST, "error.validation_failed"),
    NOT_FOUND("resource_not_found", HttpStatus.NOT_FOUND, "error.not_found"),
    UNSUPPORTED_OPERATION("unsupported_operation", HttpStatus.NOT_IMPLEMENTED, "error.unsupported_operation"),
    GATEWAY_TIMEOUT("gateway_timeout", HttpStatus.GATEWAY_TIMEOUT, "error.gateway_timeout"),
    RATE_LIMIT_EXCEEDED("rate_limit_exceeded", HttpStatus.TOO_MANY_REQUESTS, "error.rate_limit_exceeded"),
    UNSUPPORTED_API_VERSION("unsupported_api_version", HttpStatus.GATEWAY_TIMEOUT,
        "error.unsupported_api_version");


    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String messageKey;

    public static Optional<ErrorCode> fromErrorCode(String errorCode) {
        return Arrays.stream(values())
            .filter(code -> code.errorCode().equals(errorCode))
            .findFirst();
    }
}
