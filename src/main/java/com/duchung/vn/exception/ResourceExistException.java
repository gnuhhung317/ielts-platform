package com.duchung.vn.exception;

import org.springframework.http.HttpStatus;

public class UsernameExistException extends CustomException{
    public UsernameExistException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
