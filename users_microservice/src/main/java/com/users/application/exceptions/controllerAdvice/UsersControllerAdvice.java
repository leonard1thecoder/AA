package com.users.application.exceptions.controllerAdvice;

import com.users.application.exceptions.*;
import com.utils.application.ExceptionHandlerReporter;
import com.utils.application.globalExceptions.errorResponse.ErrorResponse;
import org.hibernate.HibernateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;


@ControllerAdvice
public class UsersControllerAdvice extends ExceptionHandlerReporter {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> manageUserNotFoundException(){
        return new ResponseEntity<>(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAgeException.class)
    public ResponseEntity<ErrorResponse> manageUserAgeException(){
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


    @ExceptionHandler(HibernateException.class)
    public ResponseEntity<ErrorResponse> manageConnectionTimeout(){
        return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now().toString(), "Reconnect","---------------Connection timeout------"),HttpStatus.FORBIDDEN);
    }
}
