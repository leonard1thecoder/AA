package com.utils.application.globalExceptions;

public class ServiceRunnerNotFoundException extends RuntimeException {
    public ServiceRunnerNotFoundException(String message) {
        super(message);
    }
}
