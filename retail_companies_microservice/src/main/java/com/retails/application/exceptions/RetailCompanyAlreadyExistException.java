package com.retails.application.exceptions;

public class RetailCompanyAlreadyExistException extends RuntimeException {
    public RetailCompanyAlreadyExistException(String message) {
        super(message);
    }
}
