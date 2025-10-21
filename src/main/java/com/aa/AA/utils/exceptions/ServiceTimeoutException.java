package com.aa.AA.utils.exceptions;

import java.util.concurrent.*;

public class ServiceTimeoutException extends TimeoutException {
    public ServiceTimeoutException(String message) {
        super(message);
    }
}
