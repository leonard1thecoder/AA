package com.users.application.exceptions;

public class VerificationTokenIncorrectException extends RuntimeException {
    public VerificationTokenIncorrectException(String message) {
        super(message);
    }
}
