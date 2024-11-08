package io.github.susimsek.springnextjssamples.exception.graphql;

import graphql.ErrorClassification;

public enum ErrorType implements ErrorClassification {
    BAD_REQUEST,
    UNAUTHORIZED,
    FORBIDDEN,
    NOT_FOUND,
    INTERNAL_ERROR,
    THROTTLED,
    CONFLICT;

    private ErrorType() {
    }
}
