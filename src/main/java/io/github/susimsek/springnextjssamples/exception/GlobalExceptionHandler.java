package io.github.susimsek.springnextjssamples.exception;

import static io.github.susimsek.springnextjssamples.constant.RateLimitConstants.RATE_LIMIT_LIMIT_HEADER_NAME;
import static io.github.susimsek.springnextjssamples.constant.RateLimitConstants.RATE_LIMIT_REMAINING_HEADER_NAME;
import static io.github.susimsek.springnextjssamples.constant.RateLimitConstants.RATE_LIMIT_RESET_HEADER_NAME;

import io.github.susimsek.springnextjssamples.exception.ratelimit.RateLimitExceededException;
import io.github.susimsek.springnextjssamples.exception.versioning.UnsupportedApiVersionException;
import io.github.susimsek.springnextjssamples.config.i18n.ParameterMessageSource;
import jakarta.validation.ConstraintViolationException;
import java.net.SocketTimeoutException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Global exception handler that intercepts various exceptions and provides structured
 * error responses across the application.
 * <p>
 * This class extends {@link ResponseEntityExceptionHandler} and handles both specific and
 * generic exceptions, mapping them to HTTP responses with meaningful error messages
 * and status codes.
 * </p>
 */
@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /** Message source used for localizing error messages. */
    private final ParameterMessageSource messageSource;

    /**
     * Handles {@link HttpMediaTypeNotAcceptableException}.
     *
     * @param ex the exception to handle
     * @param headers the HTTP headers
     * @param status the HTTP status
     * @param request the current request
     * @return a structured response entity with error details
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
        @NonNull HttpMediaTypeNotAcceptableException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        ErrorCode errorCode = ErrorCode.NOT_ACCEPTABLE;
        return createProblemDetailResponse(ex, errorCode,
            null, new HttpHeaders(), request);
    }

    /**
     * Handles {@link HttpMediaTypeNotSupportedException}, which is thrown when the media type
     * of the request is not supported by the server.
     * <p>
     * This method creates a structured error response using the {@link ErrorCode#UNSUPPORTED_MEDIA_TYPE}
     * error code, providing the appropriate HTTP headers and status.
     * </p>
     *
     * @param ex the exception to handle
     * @param headers the HTTP headers to be included in the response
     * @param status the HTTP status to be set in the response
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
        @NonNull HttpMediaTypeNotSupportedException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        ErrorCode errorCode = ErrorCode.UNSUPPORTED_MEDIA_TYPE;
        return createProblemDetailResponse(ex,
            errorCode,
            null, new HttpHeaders(), request);
    }

    /**
     * Handles {@link HttpRequestMethodNotSupportedException}, which is thrown when a request
     * method is not supported by the targeted endpoint.
     * <p>
     * This method constructs a structured error response using the {@link ErrorCode#METHOD_NOT_ALLOWED}
     * error code, indicating that the HTTP method used in the request is not permitted.
     * </p>
     *
     * @param ex the exception to handle
     * @param headers the HTTP headers to be included in the response
     * @param status the HTTP status to be set in the response
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
        @NonNull HttpRequestMethodNotSupportedException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;
        return createProblemDetailResponse(ex,
            errorCode,
            null, new HttpHeaders(), request);
    }

    /**
     * Handles {@link HttpMessageNotReadableException}, which is thrown when a request's
     * body cannot be read or parsed correctly, often due to malformed JSON or incorrect content.
     * <p>
     * This method creates a structured error response using the {@link ErrorCode#MESSAGE_NOT_READABLE}
     * error code, providing relevant details to help the client understand the issue with the request body.
     * </p>
     *
     * @param ex the exception to handle
     * @param headers the HTTP headers to be included in the response
     * @param status the HTTP status to be set in the response
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        @NonNull HttpMessageNotReadableException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        ErrorCode errorCode = ErrorCode.MESSAGE_NOT_READABLE;
        return createProblemDetailResponse(ex,
            errorCode,
            null, new HttpHeaders(), request);
    }

    /**
     * Handles {@link TypeMismatchException}, which is thrown when a request parameter
     * or path variable cannot be converted to the expected type.
     * <p>
     * This method generates a structured error response using the {@link ErrorCode#TYPE_MISMATCH}
     * error code, informing the client about the type mismatch issue in the request.
     * </p>
     *
     * @param ex the exception to handle
     * @param headers the HTTP headers to be included in the response
     * @param status the HTTP status to be set in the response
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response
     */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
        @NonNull TypeMismatchException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        ErrorCode errorCode = ErrorCode.TYPE_MISMATCH;
        return createProblemDetailResponse(ex,
            errorCode,
            null, new HttpHeaders(), request);
    }

    /**
     * Handles {@link MissingServletRequestParameterException}, which is thrown when a required
     * request parameter is missing from the request.
     * <p>
     * This method constructs a structured error response using the {@link ErrorCode#MISSING_PARAMETER}
     * error code, along with details about the missing parameter to help the client identify the issue.
     * </p>
     *
     * @param ex the exception to handle
     * @param headers the HTTP headers to be included in the response
     * @param status the HTTP status to be set in the response
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
        @NonNull MissingServletRequestParameterException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        Map<String, Object> namedArgs = Map.of("paramName", ex.getParameterName());
        ErrorCode errorCode = ErrorCode.MISSING_PARAMETER;
        return createProblemDetailResponse(ex, errorCode,
            namedArgs, new HttpHeaders(), request);
    }

    /**
     * Handles {@link MissingServletRequestPartException}, which is thrown when a required
     * part of a multipart request is missing.
     * <p>
     * This method generates a structured error response using the {@link ErrorCode#MISSING_PART}
     * error code, providing details about the missing part to assist the client in correcting the request.
     * </p>
     *
     * @param ex the exception to handle
     * @param headers the HTTP headers to be included in the response
     * @param status the HTTP status to be set in the response
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
        @NonNull MissingServletRequestPartException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        Map<String, Object> namedArgs = Map.of("partName", ex.getRequestPartName());
        ErrorCode errorCode = ErrorCode.MISSING_PART;
        return createProblemDetailResponse(ex, errorCode,
            namedArgs, new HttpHeaders(), request);
    }

    /**
     * Handles {@link ServletRequestBindingException}, which is thrown when there is an issue
     * with binding a request parameter, header, or other element in a servlet request.
     * <p>
     * This method creates a structured error response using the {@link ErrorCode#REQUEST_BINDING}
     * error code, indicating that there was a problem with request binding.
     * </p>
     *
     * @param ex the exception to handle
     * @param headers the HTTP headers to be included in the response
     * @param status the HTTP status to be set in the response
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response
     */
    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
        @NonNull ServletRequestBindingException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        ErrorCode errorCode = ErrorCode.REQUEST_BINDING;
        return createProblemDetailResponse(ex, errorCode,
            null, new HttpHeaders(), request);
    }

    /**
     * Handles {@link MaxUploadSizeExceededException}, which is thrown when an uploaded file
     * exceeds the maximum allowed size.
     * <p>
     * This method constructs a structured error response using the {@link ErrorCode#PAYLOAD_TOO_LARGE}
     * error code, informing the client that the uploaded file's size is too large to be processed.
     * </p>
     *
     * @param ex the exception to handle
     * @param headers the HTTP headers to be included in the response
     * @param status the HTTP status to be set in the response
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response
     */
    @Override
    protected ResponseEntity<Object> handleMaxUploadSizeExceededException(
        @NonNull MaxUploadSizeExceededException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        ErrorCode errorCode = ErrorCode.PAYLOAD_TOO_LARGE;
        return createProblemDetailResponse(ex, errorCode,
            null, new HttpHeaders(), request);
    }

    /**
     * Handles {@link MultipartException}, which is thrown when an error occurs
     * during a multipart file upload process.
     * <p>
     * This method constructs a structured error response using the {@link ErrorCode#MULTIPART}
     * error code, providing information about the multipart processing error to the client.
     * </p>
     *
     * @param ex the exception to handle
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response
     */
    @ExceptionHandler(MultipartException.class)
    protected ResponseEntity<Object> handleMultipartException(@NonNull MultipartException ex,
                                                              @NonNull WebRequest request) {
        ErrorCode errorCode = ErrorCode.MULTIPART;
        return createProblemDetailResponse(ex, errorCode,
            null, new HttpHeaders(), request);
    }

    /**
     * Handles {@link MethodArgumentNotValidException}, which is thrown when method arguments
     * fail validation constraints.
     * <p>
     * This method creates a structured error response using the {@link ErrorCode#VALIDATION_FAILED}
     * error code, along with a list of detailed validation violations to help the client understand
     * the specific issues with the request.
     * </p>
     *
     * @param ex the exception to handle, containing validation errors
     * @param headers the HTTP headers to be included in the response
     * @param status the HTTP status to be set in the response
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response, including a list of violations
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        @NonNull MethodArgumentNotValidException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        List<Violation> violations = Stream.concat(
            ex.getFieldErrors().stream().map(Violation::new),
            ex.getGlobalErrors().stream().map(Violation::new)
        ).toList();

        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        ProblemDetail problem = createProblemDetail(errorCode,
            null, request);
        problem.setProperty(ErrorConstants.PROBLEM_VIOLATION_KEY, violations);
        return handleExceptionInternal(ex, problem, headers, errorCode.httpStatus(), request);
    }

    /**
     * Handles {@link ConstraintViolationException}, which is thrown when a constraint
     * validation fails, typically during parameter validation.
     * <p>
     * This method generates a structured error response using the {@link ErrorCode#VALIDATION_FAILED}
     * error code, providing a list of specific validation violations to help the client
     * understand the issues with the request.
     * </p>
     *
     * @param ex the exception to handle, containing constraint violations
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response, including a list of violations
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(
        @NonNull ConstraintViolationException ex,
        @NonNull WebRequest request) {
        List<Violation> violations = ex.getConstraintViolations().stream().map(Violation::new).toList();

        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        ProblemDetail problem = createProblemDetail(errorCode,
            null, request);
        problem.setProperty(ErrorConstants.PROBLEM_VIOLATION_KEY, violations);
        return handleExceptionInternal(ex, problem, new HttpHeaders(), errorCode.httpStatus(), request);
    }

    /**
     * Handles {@link AuthenticationException}, which is thrown when authentication fails,
     * typically due to invalid credentials or token issues.
     * <p>
     * This method generates a structured error response using the {@link ErrorCode#INVALID_TOKEN}
     * error code, informing the client about the authentication failure.
     * </p>
     *
     * @param ex the exception to handle, containing details about the authentication failure
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response
     */
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthentication(@NonNull AuthenticationException ex,
                                                          @NonNull WebRequest request) {
        ErrorCode errorCode = ErrorCode.INVALID_TOKEN;
        return createProblemDetailResponse(ex, errorCode,
            null, new HttpHeaders(), request);
    }

    /**
     * Handles {@link AccessDeniedException}, which is thrown when a user attempts to access
     * a resource or perform an action for which they lack sufficient permissions.
     * <p>
     * This method generates a structured error response using the {@link ErrorCode#ACCESS_DENIED}
     * error code, informing the client that access to the requested resource is denied.
     * </p>
     *
     * @param ex the exception to handle, containing details about the access denial
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDenied(@NonNull AccessDeniedException ex,
                                                        @NonNull WebRequest request) {
        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
        return createProblemDetailResponse(ex, errorCode,
            null, new HttpHeaders(), request);
    }

    /**
     * Handles {@link NoHandlerFoundException}, which is thrown when a request is made to an
     * endpoint that does not exist or cannot be mapped to a handler.
     * <p>
     * This method generates a structured error response using the {@link ErrorCode#NOT_FOUND}
     * error code, informing the client that the requested resource could not be found.
     * </p>
     *
     * @param ex the exception to handle, containing details about the missing handler
     * @param headers the HTTP headers to be included in the response
     * @param status the HTTP status to be set in the response
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
        @NonNull NoHandlerFoundException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND;
        return createProblemDetailResponse(ex, errorCode,
            null, new HttpHeaders(), request);
    }

    /**
     * Handles {@link NoResourceFoundException}, which is thrown when a requested resource
     * cannot be located.
     * <p>
     * This method generates a structured error response using the {@link ErrorCode#NOT_FOUND}
     * error code, notifying the client that the specified resource does not exist.
     * </p>
     *
     * @param ex the exception to handle, containing details about the missing resource
     * @param headers the HTTP headers to be included in the response
     * @param status the HTTP status to be set in the response
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response
     */
    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(
        @NonNull NoResourceFoundException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND;
        return createProblemDetailResponse(ex, errorCode,
            null, new HttpHeaders(), request);
    }

    /**
     * Handles {@link RateLimitExceededException}, which is thrown when a client exceeds
     * the allowed rate limit for requests.
     * <p>
     * This method generates a structured error response using the {@link ErrorCode#RATE_LIMIT_EXCEEDED}
     * error code and includes headers to inform the client about rate limit details, such as the
     * wait time, limit for the period, remaining permissions, and reset time.
     * </p>
     *
     * @param ex the exception to handle, containing details about the rate limit breach
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response with rate limit headers
     */
    @ExceptionHandler(RateLimitExceededException.class)
    protected ResponseEntity<Object> handleRateLimitExceededException(
        @NonNull RateLimitExceededException ex,
        @NonNull WebRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.RETRY_AFTER, String.valueOf(ex.getWaitTime()));
        headers.add(RATE_LIMIT_LIMIT_HEADER_NAME, String.valueOf(ex.getLimitForPeriod()));
        headers.add(RATE_LIMIT_REMAINING_HEADER_NAME, String.valueOf(ex.getAvailablePermissions()));
        headers.add(RATE_LIMIT_RESET_HEADER_NAME, String.valueOf(ex.getResetTime()));

        ErrorCode errorCode = ErrorCode.RATE_LIMIT_EXCEEDED;
        return createProblemDetailResponse(ex, errorCode,
            null, headers, request);
    }

    /**
     * Handles {@link UnsupportedOperationException}, which is thrown when an unsupported
     * or unimplemented operation is invoked in the application.
     * <p>
     * This method generates a structured error response using the {@link ErrorCode#UNSUPPORTED_OPERATION}
     * error code, notifying the client that the requested operation is not supported.
     * </p>
     *
     * @param ex the exception to handle, containing details about the unsupported operation
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response
     */
    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<Object> handleUnsupportedOperationException(
        @NonNull UnsupportedOperationException ex,
        @NonNull WebRequest request) {
        ErrorCode errorCode = ErrorCode.UNSUPPORTED_OPERATION;
        return createProblemDetailResponse(ex, errorCode,
            null, new HttpHeaders(), request);
    }

    /**
     * Handles {@link UnsupportedApiVersionException}, which is thrown when a client
     * requests an unsupported API version.
     * <p>
     * This method generates a structured error response using the {@link ErrorCode#UNSUPPORTED_API_VERSION}
     * error code, notifying the client that the requested API version is not supported.
     * </p>
     *
     * @param ex the exception to handle, containing details about the unsupported API version
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response
     */
    @ExceptionHandler(UnsupportedApiVersionException.class)
    public ResponseEntity<Object> handleUnsupportedApiVersionException(
        @NonNull UnsupportedApiVersionException ex,
        @NonNull WebRequest request) {
        ErrorCode errorCode = ErrorCode.UNSUPPORTED_API_VERSION;
        return createProblemDetailResponse(ex, errorCode,
            null, new HttpHeaders(), request);
    }

    /**
     * Handles {@link SocketTimeoutException}, which is thrown when a socket connection
     * times out while attempting to communicate with an external service.
     * <p>
     * This method generates a structured error response using the {@link ErrorCode#GATEWAY_TIMEOUT}
     * error code, indicating that the request timed out while waiting for a response from an external service.
     * </p>
     *
     * @param ex the exception to handle, containing details about the timeout error
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response
     */
    @ExceptionHandler(SocketTimeoutException.class)
    public ResponseEntity<Object> handleSocketTimeoutException(@NonNull SocketTimeoutException ex,
                                                               @NonNull WebRequest request) {
        ErrorCode errorCode = ErrorCode.GATEWAY_TIMEOUT;
        return createProblemDetailResponse(ex, errorCode,
            null, new HttpHeaders(), request);
    }

    /**
     * Handles {@link AsyncRequestTimeoutException}, which is thrown when an asynchronous
     * request exceeds its allotted time to complete.
     * <p>
     * This method generates a structured error response using the {@link ErrorCode#TEMPORARILY_UNAVAILABLE}
     * error code, notifying the client that the request could not be completed in the expected time frame.
     * </p>
     *
     * @param ex the exception to handle, containing details about the timeout
     * @param headers the HTTP headers to be included in the response
     * @param status the HTTP status to be set in the response
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response
     */
    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(
        @NonNull AsyncRequestTimeoutException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        ErrorCode errorCode = ErrorCode.TEMPORARILY_UNAVAILABLE;
        return createProblemDetailResponse(ex, errorCode,
            null, new HttpHeaders(), request);
    }

    /**
     * Handles {@link ResourceException}, which is thrown when an issue occurs with resource
     * retrieval or manipulation, typically due to missing or invalid data.
     * <p>
     * This method generates a structured error response, using the localized resource name and
     * search criteria from the exception details. It provides an error message in the appropriate locale
     * to help the client understand the specific issue with the requested resource.
     * </p>
     *
     * @param ex the exception to handle, containing details about the resource error
     * @param request the current {@link WebRequest} being processed, providing locale information
     * @return a {@link ResponseEntity} containing the structured error response with localized details
     */
    @ExceptionHandler(ResourceException.class)
    public ResponseEntity<Object> handleResourceException(@NonNull ResourceException ex,
                                                          @NonNull WebRequest request) {
        Locale locale = request.getLocale();
        String localizedResourceName = messageSource.getMessage(
            "resource." + ex.getResourceName().toLowerCase(), null, locale);
        String searchCriteria = messageSource.getMessage(
            "search.criteria." + ex.getSearchCriteria().toLowerCase(), null, locale);
        Map<String, Object> namedArgs = createNamedArgs(
            localizedResourceName, searchCriteria, ex.getSearchValue());
        return createProblemDetailResponse(ex, ex.getErrorCode(),
            ex.getMessage(), ex.getStatus(),
            namedArgs, new HttpHeaders(), request);
    }

    /**
     * Handles {@link LocalizedException}, which is a custom exception type designed to provide
     * localized error messages and arguments for more user-friendly error responses.
     * <p>
     * This method generates a structured error response using the error code, message, status,
     * and named arguments from the exception, offering a detailed and localized error message
     * to the client.
     * </p>
     *
     * @param ex the exception to handle, containing localized error details
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured and localized error response
     */
    @ExceptionHandler(LocalizedException.class)
    public ResponseEntity<Object> handleLocalizedException(@NonNull LocalizedException ex,
                                                           @NonNull WebRequest request) {
        return createProblemDetailResponse(ex, ex.getErrorCode(),
            ex.getMessage(), ex.getStatus(),
            ex.getNamedArgs(), new HttpHeaders(), request);
    }

    /**
     * Handles all uncaught exceptions, providing a generic error response for unexpected server errors.
     * <p>
     * This method generates a structured error response using the {@link ErrorCode#SERVER_ERROR}
     * error code, ensuring that any unhandled exceptions are communicated to the client in a
     * standardized format.
     * </p>
     *
     * @param ex the exception to handle, representing an unexpected error
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response for a server error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(
        @NonNull Exception ex,
        @NonNull WebRequest request) {
        ErrorCode errorCode = ErrorCode.SERVER_ERROR;
        return createProblemDetailResponse(ex, errorCode,
            null, new HttpHeaders(), request);
    }

    /**
     * Handles internal exceptions by logging the exception and forwarding the response handling
     * to the superclass's {@link ResponseEntityExceptionHandler#handleExceptionInternal} method.
     * <p>
     * This method logs the exception and the corresponding HTTP status before delegating
     * to the superclass, allowing for consistent error handling and logging for internal exceptions.
     * </p>
     *
     * @param ex the exception to handle, containing details about the internal error
     * @param body the body of the response, potentially containing additional error information
     * @param headers the HTTP headers to be included in the response
     * @param status the HTTP status to be set in the response
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
        @NonNull Exception ex,
        Object body,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        log.error("An exception occurred, which will cause a {} response", status, ex);
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * Creates a structured error response for a given exception and error code.
     * <p>
     * This method uses the provided {@link ErrorCode} to generate a detailed problem response,
     * including the error code, message key, HTTP status, and any named arguments
     * for localization or additional error context.
     * </p>
     *
     * @param ex the exception to handle, providing context for the error
     * @param errorCode the error code representing the type of error
     * @param namedArgs a map of named arguments for localized message formatting
     * @param headers the HTTP headers to be included in the response
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response
     */
    private ResponseEntity<Object> createProblemDetailResponse(
        Exception ex,
        ErrorCode errorCode,
        Map<String, Object> namedArgs,
        HttpHeaders headers,
        WebRequest request) {
        return createProblemDetailResponse(ex, errorCode.errorCode(),
            errorCode.messageKey(), errorCode.httpStatus(),
            namedArgs, headers, request);
    }

    /**
     * Creates a structured error response for a given exception, using detailed error information.
     * <p>
     * This method constructs a {@link ProblemDetail} object with the provided error code, message key,
     * HTTP status, named arguments, and request context. The method then forwards the response
     * to {@link #handleExceptionInternal} for further processing.
     * </p>
     *
     * @param ex the exception to handle, providing context for the error
     * @param errorCode the specific error code identifying the error type
     * @param messageKey the message key for localized error messages
     * @param status the HTTP status to be set in the response
     * @param namedArgs a map of named arguments for message formatting
     * @param headers the HTTP headers to be included in the response
     * @param request the current {@link WebRequest} being processed
     * @return a {@link ResponseEntity} containing the structured error response
     */
    private ResponseEntity<Object> createProblemDetailResponse(
        Exception ex,
        String errorCode,
        String messageKey,
        HttpStatusCode status,
        Map<String, Object> namedArgs,
        HttpHeaders headers,
        WebRequest request) {
        ProblemDetail problem = createProblemDetail(
            errorCode, messageKey, status,
            namedArgs, request);
        return handleExceptionInternal(ex, problem,
            headers, status, request);
    }

    /**
     * Creates a {@link ProblemDetail} object using the specified {@link ErrorCode} and additional
     * named arguments for message localization.
     * <p>
     * This method uses the provided {@link ErrorCode} to build a `ProblemDetail` object, including
     * the error code, message key, HTTP status, and any named arguments for context or localization.
     * </p>
     *
     * @param errorCode the {@link ErrorCode} representing the specific type of error
     * @param namedArgs a map of named arguments for message localization or additional context
     * @param request the current {@link WebRequest} being processed, providing context for localization
     * @return a {@link ProblemDetail} object containing structured error details
     */
    private ProblemDetail createProblemDetail(ErrorCode errorCode,
                                              Map<String, Object> namedArgs,
                                              WebRequest request) {
        return createProblemDetail(errorCode.errorCode(),
            errorCode.messageKey(),errorCode.httpStatus(), namedArgs, request);
    }

    /**
     * Creates a {@link ProblemDetail} object with the specified error details, including
     * an error code, message key, HTTP status, and named arguments for localized message formatting.
     * <p>
     * This method retrieves a localized error message using the {@code messageKey} and {@code namedArgs},
     * then constructs a {@link ProblemDetail} object with the generated message and HTTP status.
     * The error code is added as a property for further identification.
     * </p>
     *
     * @param errorCode the unique code representing the specific type of error
     * @param messageKey the key used to retrieve the localized error message
     * @param status the HTTP status to be associated with this error
     * @param namedArgs a map of named arguments used for formatting the localized message
     * @param request the current {@link WebRequest}, providing locale information for localization
     * @return a {@link ProblemDetail} object containing the structured and localized error details
     */
    private ProblemDetail createProblemDetail(String errorCode,
                                              String messageKey,
                                              HttpStatusCode status,
                                              Map<String, Object> namedArgs, WebRequest request) {
        String errorMessage = messageSource.getMessageWithNamedArgs(
            messageKey, namedArgs, request.getLocale());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            status, errorMessage);
        problem.setProperty("error", errorCode);
        return problem;
    }

    /**
     * Creates a map of named arguments to provide context for localized error messages,
     * typically used to describe a missing or invalid resource.
     * <p>
     * This method constructs a map with keys for the resource name, search criteria,
     * and search value, allowing for more detailed and specific error messages.
     * </p>
     *
     * @param resourceName the name of the resource involved in the error
     * @param searchCriteria the criteria used to search for or identify the resource
     * @param searchValue the value used in the search, providing additional context
     * @return a {@link Map} containing the named arguments for localization or additional detail
     */
    private Map<String, Object> createNamedArgs(String resourceName,
                                                String searchCriteria,
                                                Object searchValue) {
        Map<String, Object> namedArgs = new HashMap<>();
        namedArgs.put("resource", resourceName);
        namedArgs.put("criteria", searchCriteria);
        namedArgs.put("value", searchValue.toString());
        return namedArgs;
    }
}
