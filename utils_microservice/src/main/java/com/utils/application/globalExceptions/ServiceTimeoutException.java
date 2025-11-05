package com.utils.application.globalExceptions;


public class ServiceTimeoutException extends RuntimeException {
    public ServiceTimeoutException(String message) {
        super(message);
    }
}
