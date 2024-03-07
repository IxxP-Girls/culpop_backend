package com.ixxp.culpop.util.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestApiException {
    private final HttpStatus status;
    private final String errorCode;
    private final String errorMessage;

    public RestApiException(HttpStatus status, String errorCode, String errorMessage) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
