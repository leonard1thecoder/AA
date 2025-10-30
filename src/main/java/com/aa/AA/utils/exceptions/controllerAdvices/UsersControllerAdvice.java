package com.aa.AA.utils.exceptions.controllerAdvices;

import com.aa.AA.dtos.ErrorResponse;
import com.aa.AA.utils.exceptions.*;
import com.aa.AA.utils.exceptions.exceptionHandler.ExceptionHandlerReporter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class UsersControllerAdvice extends ExceptionHandlerReporter {


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> manageUserNotFoundException(){
        return new ResponseEntity<>(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserUnderAgeOf18Exception.class)
    public ResponseEntity<ErrorResponse> manageUserUnderAgeOf18Exception(){
        return new ResponseEntity<>(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()),HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UsersExistsException.class)
    public ResponseEntity<ErrorResponse> manageUsersExistsException(){
        return new ResponseEntity<>(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()),HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PasswordMisMatchException.class)
    public ResponseEntity<ErrorResponse> managePasswordMisMatchException(){
        return new ResponseEntity<>(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()),HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserEmailDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> manageUserEmailDoesNotExistException(){
        return new ResponseEntity<>(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CachedUsersPasswordChangedException.class)
    public ResponseEntity<ErrorResponse> manageCachedUsersPasswordChangedException(){
        return new ResponseEntity<>(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()),HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UsersPasswordIncorrectException.class)
    public ResponseEntity<ErrorResponse> manageUsersPasswordIncorrectException(){
        return new ResponseEntity<>(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()),HttpStatus.FORBIDDEN);
    }

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
