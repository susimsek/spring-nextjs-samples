package io.github.susimsek.springnextjssamples.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested resource is not found.
 * <p>
 * This exception extends {@link ResourceException} and is typically used to indicate
 * that a resource could not be located based on the provided search criteria. The exception
 * response is associated with the HTTP status code {@link HttpStatus#NOT_FOUND} (404 Not Found).
 * </p>
 */
public class ResourceNotFoundException extends ResourceException {

    /**
     * Constructs a new {@code ResourceNotFoundException} with the specified resource details.
     *
     * @param resourceName the name of the resource that was not found
     * @param searchCriteria the search criteria used to attempt locating the resource
     * @param searchValue the specific value used in the search that led to the exception
     */
    public ResourceNotFoundException(String resourceName, String searchCriteria, Object searchValue) {
        super("resource_not_found", "error.resource_not_found",
            HttpStatus.NOT_FOUND, resourceName, searchCriteria, searchValue);
    }
}
