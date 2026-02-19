package com.payment.application.exceptions;

public class PaymentStrategyNotFoundException extends RuntimeException {
    public PaymentStrategyNotFoundException(String message) {
        super(message);
    }
}
