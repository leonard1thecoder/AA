package com.utils.application.controllerAdvice;


import com.utils.application.globalExceptions.errorResponse.*;

import com.utils.application.ExceptionHandlerReporter;
import com.utils.application.globalExceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExecutorControllerAdvice extends ExceptionHandlerReporter {



    @ExceptionHandler(ServiceTimeoutException.class)
    public ResponseEntity<ErrorResponse> manageServiceTimeoutException(){
        return new ResponseEntity<>(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()),HttpStatus.GATEWAY_TIMEOUT);
    }

    @ExceptionHandler(ServiceInterruptedException.class)
    public ResponseEntity<ErrorResponse> manageServiceInterruptedException(){
        return new ResponseEntity<>(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()),HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(ServiceExecutionException.class)
    public ResponseEntity<ErrorResponse> manageServiceExecutionException(){
        return new ResponseEntity<>(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()),HttpStatus.BAD_GATEWAY);
    }

}
