package io.github.susimsek.springnextjssamples.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

/**
 * A base exception class for handling errors related to resource operations.
 * <p>
 * This exception is typically thrown when an operation on a specific resource fails,
 * such as when a resource cannot be found or when there is a conflict. It includes
 * additional fields to store details about the resource, including the resource name,
 * search criteria, and the specific value that caused the error.
 * </p>
 */
@Getter
public class ResourceException extends RuntimeException {

    /** The unique error code representing the type of resource exception. */
    private final String errorCode;

    /** The HTTP status to be returned in the response for this exception. */
    private final HttpStatusCode status;

    /** The name of the resource associated with this exception. */
    private final String resourceName;

    /** The criteria used to search or identify the resource involved in the error. */
    private final String searchCriteria;

    /** The specific value used in the search criteria that led to the exception. */
    private final transient Object searchValue;

    /**
     * Constructs a new {@code ResourceException} with the specified error code, message,
     * HTTP status, and resource details.
     *
     * @param errorCode the unique error code representing this resource error
     * @param message the error message, typically a key for localization
     * @param status the HTTP status code to be returned in the response
     * @param resourceName the name of the resource associated with this exception
     * @param searchCriteria the search criteria used to locate or identify the resource
     * @param searchValue the specific value used in the search that caused the exception
     */
    public ResourceException(String errorCode, String message, HttpStatusCode status,
                             String resourceName, String searchCriteria, Object searchValue) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
        this.resourceName = resourceName;
        this.searchCriteria = searchCriteria;
        this.searchValue = searchValue;
    }
}
