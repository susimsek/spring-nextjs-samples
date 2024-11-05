package io.github.susimsek.springnextjssamples.web.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ResourceException {

    public ResourceNotFoundException(String resourceName, String searchCriteria, Object searchValue) {
        super("resource_not_found", "error.resource_not_found",
            HttpStatus.NOT_FOUND, resourceName, searchCriteria, searchValue);
    }

}
