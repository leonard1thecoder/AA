package com.users.application.exceptions;

public class NullRequestException extends RuntimeException {
    public NullRequestException(String message) {
        super(message);
    }
}
