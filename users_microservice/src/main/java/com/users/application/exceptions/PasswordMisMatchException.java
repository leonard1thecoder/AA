package com.users.application.exceptions;

public class PasswordMisMatchException extends RuntimeException {
    public PasswordMisMatchException(String message) {
        super(message);
    }
}
