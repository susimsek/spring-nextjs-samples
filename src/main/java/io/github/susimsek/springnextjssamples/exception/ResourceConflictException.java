package io.github.susimsek.springnextjssamples.exception;

import org.springframework.http.HttpStatus;

public class ResourceConflictException extends ResourceException {
    public ResourceConflictException(String resourceName, String searchCriteria, Object searchValue) {
        super("resource_conflict", "error.resource_conflict",
            HttpStatus.CONFLICT, resourceName, searchCriteria, searchValue);
    }
}
