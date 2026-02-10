package com.retails.followers.application.exceptions;

public class FollowingRecordNotFoundException extends RuntimeException {
    public FollowingRecordNotFoundException(String message) {
        super(message);
    }
}
