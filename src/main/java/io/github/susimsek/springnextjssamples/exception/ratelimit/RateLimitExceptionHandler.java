package io.github.susimsek.springnextjssamples.exception.ratelimit;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Interface for handling rate limit exceptions within the application.
 * <p>
 * This interface defines a method for managing {@link RateLimitExceededException} instances,
 * allowing implementations to specify custom logic for handling scenarios where
 * a client has exceeded the permitted rate limit.
 * </p>
 */
public interface RateLimitExceptionHandler {

    /**
     * Handles a {@link RateLimitExceededException} by applying appropriate response logic.
     *
     * @param request the {@link HttpServletRequest} that led to the exception
     * @param response the {@link HttpServletResponse} used to send the response to the client
     * @param exception the {@link RateLimitExceededException} that was thrown
     * @throws IOException if an I/O error occurs while handling the exception
     * @throws ServletException if a servlet-related error occurs while handling the exception
     */
    void handle(HttpServletRequest request, HttpServletResponse response,
                RateLimitExceededException exception) throws IOException, ServletException;
}
