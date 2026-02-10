package com.retails.followers.application.exceptions;

public class UserAlreadyFollowedRetailCompanyException extends RuntimeException {
    public UserAlreadyFollowedRetailCompanyException(String message) {
        super(message);
    }
}
