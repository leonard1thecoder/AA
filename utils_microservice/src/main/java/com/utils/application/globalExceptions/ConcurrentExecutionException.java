package com.utils.application.globalExceptions;

public class ConcurrentExecutionException extends RuntimeException {
    public ConcurrentExecutionException(String message) {
        super(message);
    }
}
