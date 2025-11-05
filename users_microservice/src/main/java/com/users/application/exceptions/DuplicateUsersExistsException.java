package com.users.application.exceptions;

public class DuplicateUsersExistsException extends RuntimeException {
    public DuplicateUsersExistsException(String message) {
        super(message);
    }
}
