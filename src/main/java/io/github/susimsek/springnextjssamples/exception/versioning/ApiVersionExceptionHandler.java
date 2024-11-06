package io.github.susimsek.springnextjssamples.exception.versioning;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Interface for handling exceptions related to unsupported API versions.
 * <p>
 * This interface defines a method for managing the {@link UnsupportedApiVersionException},
 * allowing implementations to specify custom logic for handling unsupported API version errors.
 * </p>
 */
public interface ApiVersionExceptionHandler {

    /**
     * Handles an {@link UnsupportedApiVersionException}, triggered when a requested API version
     * is not supported by the application.
     *
     * @param request the {@link HttpServletRequest} that led to the exception
     * @param response the {@link HttpServletResponse} for sending the error response
     * @param exception the {@link UnsupportedApiVersionException} that was thrown
     * @throws IOException if an I/O error occurs while handling the exception
     * @throws ServletException if a servlet-related error occurs while handling the exception
     */
    void handleUnsupportedApiVersionException(
        HttpServletRequest request,
        HttpServletResponse response,
        UnsupportedApiVersionException exception) throws IOException, ServletException;
}
