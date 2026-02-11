package com.product.application.exceptions;

public class NotPrivilegedToSentRequestException extends RuntimeException {
    public NotPrivilegedToSentRequestException(String message) {
        super(message);
    }
}
