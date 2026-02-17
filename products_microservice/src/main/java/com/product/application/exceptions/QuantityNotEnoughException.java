package com.product.application.exceptions;

public class QuantityNotEnoughException extends RuntimeException {
    public QuantityNotEnoughException(String message) {
        super(message);
    }
}
