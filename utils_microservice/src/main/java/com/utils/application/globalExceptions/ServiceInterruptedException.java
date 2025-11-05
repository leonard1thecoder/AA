package com.utils.application.globalExceptions;

public class ServiceInterruptedException extends RuntimeException {
    public ServiceInterruptedException(String message) {
        super(message);
    }
}
