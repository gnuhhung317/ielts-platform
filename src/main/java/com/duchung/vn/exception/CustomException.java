package com.duchung.vn.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus status;
    private final String errorCode;

    public CustomException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.errorCode = status.name();
    }
}