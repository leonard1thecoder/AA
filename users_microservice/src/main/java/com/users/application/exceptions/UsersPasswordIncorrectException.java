package com.users.application.exceptions;

public class UsersPasswordIncorrectException extends RuntimeException {
    public UsersPasswordIncorrectException(String message) {
        super(message);
    }
}
