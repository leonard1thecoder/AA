package com.aa.AA.utils.exceptions.controllerAdvices;

import com.aa.AA.dtos.ErrorResponse;
import com.aa.AA.utils.exceptions.PasswordMisMatchException;
import com.aa.AA.utils.exceptions.UsersExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.aa.AA.utils.exceptions.exceptionHandler.ExceptionHandlerReporter.*;

@RestControllerAdvice
public class RetailCompanyControllerAdvice {

    @ExceptionHandler(UsersExistsException.class)
    public ResponseEntity<ErrorResponse> manageUsersExistsException(){
        return new ResponseEntity<>(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PasswordMisMatchException.class)
    public ResponseEntity<ErrorResponse> managePasswordMisMatchException(){
        return new ResponseEntity<>(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()),HttpStatus.FORBIDDEN);
    }
}
