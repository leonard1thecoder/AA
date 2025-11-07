package com.retails.application.exceptions;

public class RetailCompanyNotFoundException extends RuntimeException {
    public RetailCompanyNotFoundException(String message) {
        super(message);
    }
}
