package io.github.susimsek.springnextjssamples.exception.versioning;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * A component for handling unsupported API version exceptions.
 * <p>
 * This class implements {@link ApiVersionExceptionHandler} to provide a centralized way to
 * handle {@link UnsupportedApiVersionException} errors by delegating to the configured
 * {@link HandlerExceptionResolver}.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class ApiVersionProblemSupport implements ApiVersionExceptionHandler {

    /** The {@link HandlerExceptionResolver} used to handle the exception. */
    private final HandlerExceptionResolver handlerExceptionResolver;

    /**
     * Handles an {@link UnsupportedApiVersionException} by delegating to the {@link HandlerExceptionResolver}.
     *
     * @param request the {@link HttpServletRequest} that led to the exception
     * @param response the {@link HttpServletResponse} for sending the error response
     * @param ex the {@link UnsupportedApiVersionException} that was thrown
     */
    @Override
    public void handleUnsupportedApiVersionException(HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     UnsupportedApiVersionException ex) {
        handlerExceptionResolver.resolveException(request, response, null, ex);
    }
}
