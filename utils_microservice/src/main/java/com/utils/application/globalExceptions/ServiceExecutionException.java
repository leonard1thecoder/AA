package com.utils.application.globalExceptions;

public class ServiceExecutionException extends RuntimeException {
    public ServiceExecutionException(String message) {
        super(message);
    }
}
