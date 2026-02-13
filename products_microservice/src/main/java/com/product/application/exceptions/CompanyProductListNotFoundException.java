package com.product.application.exceptions;

public class CompanyProductListNotFoundException extends RuntimeException {
    public CompanyProductListNotFoundException(String message) {
        super(message);
    }
}
