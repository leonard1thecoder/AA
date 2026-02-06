package com.privileges.application.exceptions;

public class PrivilegeAlreadyExistsException extends RuntimeException {
    public PrivilegeAlreadyExistsException(String message) {
        super(message);
    }
}
