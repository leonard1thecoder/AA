package com.users.application.exceptions;

public class VerifyEmailAddressException extends RuntimeException {
    public VerifyEmailAddressException(String message) {
        super(message);
    }
}
