package com.aa.AA.utils.exceptions;

public class UserEmailDoesNotExistException extends RuntimeException {
    public UserEmailDoesNotExistException(String message) {
        super(message);
    }
}
