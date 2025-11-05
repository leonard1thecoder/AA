package com.users.application.exceptions;

public class CachedUsersPasswordChangedException extends RuntimeException {
    public CachedUsersPasswordChangedException(String message) {
        super(message);
    }
}
