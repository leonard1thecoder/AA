package com.users.application.exceptions;

public class UserUnderAgeOf18Exception extends RuntimeException {
    public UserUnderAgeOf18Exception(String message) {
        super(message);
    }
}
