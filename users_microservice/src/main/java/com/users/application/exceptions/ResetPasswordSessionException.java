package com.users.application.exceptions;

public class ResetPasswordSessionException extends RuntimeException {
    public ResetPasswordSessionException(String message) {
        super(message);
    }
}
