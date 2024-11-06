package io.github.susimsek.springnextjssamples.exception.ratelimit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * A component for handling rate limit exceptions by delegating them to a configured exception resolver.
 * <p>
 * This class implements {@link RateLimitExceptionHandler} and provides a centralized approach to managing
 * {@link RateLimitExceededException} instances by utilizing the {@link HandlerExceptionResolver} to handle the
 * response.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class RateLimitProblemSupport implements RateLimitExceptionHandler {

    /** The {@link HandlerExceptionResolver} used to resolve rate limit exceptions. */
    private final HandlerExceptionResolver handlerExceptionResolver;

    /**
     * Handles a {@link RateLimitExceededException} by delegating to the {@link HandlerExceptionResolver}.
     *
     * @param request the {@link HttpServletRequest} that led to the exception
     * @param response the {@link HttpServletResponse} used to send the error response
     * @param ex the {@link RateLimitExceededException} that was thrown
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       RateLimitExceededException ex) {
        handlerExceptionResolver.resolveException(request, response, null, ex);
    }
}
