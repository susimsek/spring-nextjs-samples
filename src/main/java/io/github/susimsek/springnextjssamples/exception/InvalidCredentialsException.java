package io.github.susimsek.springnextjssamples.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends LocalizedException {

    public InvalidCredentialsException() {
        super(ErrorCode.INVALID_CREDENTIALS.errorCode(),
            ErrorCode.INVALID_CREDENTIALS.messageKey(), HttpStatus.UNAUTHORIZED);
    }
}
