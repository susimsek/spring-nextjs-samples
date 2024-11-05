package io.github.susimsek.springnextjssamples.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends LocalizedException {

    public ValidationException(String message) {
        super(ErrorCode.INVALID_REQUEST.errorCode(), message, HttpStatus.BAD_REQUEST);
    }
}
