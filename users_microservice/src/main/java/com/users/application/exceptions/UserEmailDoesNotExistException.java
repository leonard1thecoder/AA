package com.users.application.exceptions;

public class UserEmailDoesNotExistException extends RuntimeException {
    public UserEmailDoesNotExistException(String message) {
        super(message);
    }
}
