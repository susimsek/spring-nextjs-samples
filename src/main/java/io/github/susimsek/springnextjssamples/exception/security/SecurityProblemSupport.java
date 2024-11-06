package io.github.susimsek.springnextjssamples.exception.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * A component for handling security-related exceptions, such as authentication and access denied errors.
 * <p>
 * This class implements {@link AuthenticationEntryPoint} and {@link AccessDeniedHandler} to
 * provide a centralized approach to managing {@link AuthenticationException} and
 * {@link AccessDeniedException} instances by delegating them to the configured
 * {@link HandlerExceptionResolver}.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class SecurityProblemSupport implements
    AuthenticationEntryPoint, AccessDeniedHandler {

    /** The {@link HandlerExceptionResolver} used to resolve security exceptions. */
    private final HandlerExceptionResolver handlerExceptionResolver;

    /**
     * Handles an {@link AuthenticationException} by delegating to the {@link HandlerExceptionResolver}.
     * <p>
     * This method is triggered when an unauthenticated user attempts to access a protected resource.
     * </p>
     *
     * @param request the {@link HttpServletRequest} that led to the exception
     * @param response the {@link HttpServletResponse} for sending the error response
     * @param ex the {@link AuthenticationException} that was thrown
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) {
        handlerExceptionResolver.resolveException(request, response, null, ex);
    }

    /**
     * Handles an {@link AccessDeniedException} by delegating to the {@link HandlerExceptionResolver}.
     * <p>
     * This method is triggered when an authenticated user attempts to access a resource
     * for which they do not have permission.
     * </p>
     *
     * @param request the {@link HttpServletRequest} that led to the exception
     * @param response the {@link HttpServletResponse} for sending the error response
     * @param ex the {@link AccessDeniedException} that was thrown
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException ex) {
        handlerExceptionResolver.resolveException(request, response, null, ex);
    }
}
