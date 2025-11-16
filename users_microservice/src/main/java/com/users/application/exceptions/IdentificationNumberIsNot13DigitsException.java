package com.users.application.exceptions;

public class IdentificationNumberIsNot13DigitsException extends RuntimeException {
    public IdentificationNumberIsNot13DigitsException(String message) {
        super(message);
    }
}
