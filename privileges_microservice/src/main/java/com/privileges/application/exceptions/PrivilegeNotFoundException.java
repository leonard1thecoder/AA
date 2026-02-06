package com.privileges.application.exceptions;

public class PrivilegeNotFoundException extends RuntimeException {
    public PrivilegeNotFoundException(String message) {
        super(message);
    }
}
