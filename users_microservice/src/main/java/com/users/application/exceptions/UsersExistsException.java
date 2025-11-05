package com.users.application.exceptions;

public class UsersExistsException extends RuntimeException{
    public UsersExistsException(String message){
        super(message);
    }
}
