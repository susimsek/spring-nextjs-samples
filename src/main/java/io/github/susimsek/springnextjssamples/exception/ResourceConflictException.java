package io.github.susimsek.springnextjssamples.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a resource conflict occurs in the application.
 * <p>
 * This exception extends {@link ResourceException} and is typically used to indicate
 * that a requested resource conflicts with an existing resource, such as when
 * attempting to create a duplicate entry. This exception sets the HTTP status to
 * {@link HttpStatus#CONFLICT} (409 Conflict).
 * </p>
 */
public class ResourceConflictException extends ResourceException {

    /**
     * Constructs a new {@code ResourceConflictException} with the specified resource details.
     *
     * @param resourceName the name of the conflicting resource
     * @param searchCriteria the search criteria used to identify the conflict
     * @param searchValue the specific value that caused the conflict
     */
    public ResourceConflictException(String resourceName, String searchCriteria, Object searchValue) {
        super("resource_conflict", "error.resource_conflict",
            HttpStatus.CONFLICT, resourceName, searchCriteria, searchValue);
    }
}
