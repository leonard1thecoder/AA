package com.utils.application.controllerAdvice;


import com.utils.application.globalExceptions.errorResponse.*;

import com.utils.application.ExceptionHandlerReporter;
import com.utils.application.globalExceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ExecutorControllerAdvice extends ExceptionHandlerReporter {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorControllerAdvice.class);



    @ExceptionHandler(ServiceTimeoutException.class)
    public ResponseEntity<List<ErrorResponse>> manageServiceTimeoutException(){
        var list = List.of(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()));
        logger.warn("Error response : {}, error code : {}", list,HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(list, HttpStatus.FORBIDDEN);
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
