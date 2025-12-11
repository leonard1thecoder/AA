package com.users.application.exceptions.controllerAdvice;

import com.users.application.exceptions.*;
import com.utils.application.ExceptionHandlerReporter;
import com.utils.application.globalExceptions.errorResponse.ErrorResponse;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;


@ControllerAdvice
public class UsersControllerAdvice extends ExceptionHandlerReporter {
    private static final Logger logger = LoggerFactory.getLogger(UsersControllerAdvice.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<List<ErrorResponse>> manageUserNotFoundException(){
        var list = List.of(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()));
        logger.warn("Error response : {}, error code : {}", list,HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(list, HttpStatus.NOT_FOUND);
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
    public ResponseEntity<List<ErrorResponse>> manageUsersPasswordIncorrectException(){
        var list = List.of(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()));
        logger.warn("Error response : {}, error code : {}", list,HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);    }


    @ExceptionHandler(HibernateException.class)
    public ResponseEntity<ErrorResponse> manageConnectionTimeout(){
        return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now().toString(), "Reconnect","---------------Connection timeout------"),HttpStatus.FORBIDDEN);
    }
}
