package com.privileges.application.exceptions.advice;

import com.privileges.application.exceptions.PrivilegeNotFoundException;
import com.utils.application.ExceptionHandlerReporter;
import com.utils.application.globalExceptions.errorResponse.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class PrivilegeNameControllerAdvice extends ExceptionHandlerReporter {
    private static final Logger logger = LoggerFactory.getLogger(PrivilegeNameControllerAdvice.class);
    @ExceptionHandler(PrivilegeNotFoundException.class)
    public ResponseEntity<List<ErrorResponse>> managePrivilegeNotFoundException(){
        var list = List.of(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()));
        logger.warn("Error response : {}, error code : {}", list, HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(list, HttpStatus.NOT_FOUND);
    }
}
