package com.aa.AA.utils.exceptions;

import java.util.concurrent.*;

public class ServiceTimeoutException extends RuntimeException {
    public ServiceTimeoutException(String message) {
        super(message);
    }
}
