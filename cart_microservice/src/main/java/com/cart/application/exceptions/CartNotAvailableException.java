package com.cart.application.exceptions;

public class CartNotAvailableException extends RuntimeException {
    public CartNotAvailableException(String message) {
        super(message);
    }
}
