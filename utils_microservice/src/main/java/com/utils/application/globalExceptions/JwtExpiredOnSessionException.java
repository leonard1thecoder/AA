package com.utils.application.globalExceptions;

public class JwtExpiredOnSessionException extends RuntimeException {
    public JwtExpiredOnSessionException(String message) {
        super(message);
    }
}
