package com.users.application.exceptions;

public class ConcurrentExecutionException extends RuntimeException {
    public ConcurrentExecutionException(String message) {
        super(message);
    }
}
