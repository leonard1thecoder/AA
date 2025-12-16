package com.users.application.exceptions.controllerAdvice;

import com.users.application.exceptions.*;
import com.utils.application.ExceptionHandlerReporter;
import com.utils.application.globalExceptions.errorResponse.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    public ResponseEntity<List<ErrorResponse>> manageUsersExistsException(){
        var list = List.of(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()));
        logger.warn("Error response : {}, error code : {}", list,HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(list, HttpStatus.FORBIDDEN);    }

    @ExceptionHandler(PasswordMisMatchException.class)
    public ResponseEntity<ErrorResponse> managePasswordMisMatchException(){
        return new ResponseEntity<>(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()),HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserEmailDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> manageUserEmailDoesNotExistException(){
        return new ResponseEntity<>(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CachedUsersPasswordChangedException.class)
    public ResponseEntity<List<ErrorResponse>> manageCachedUsersPasswordChangedException(){
        var list = List.of(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()));
        logger.warn("Error response : {}, error code : {}", list,HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);      }

    @ExceptionHandler(UsersPasswordIncorrectException.class)
    public ResponseEntity<List<ErrorResponse>> manageUsersPasswordIncorrectException(){
        var list = List.of(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()));
        logger.warn("Error response : {}, error code : {}", list,HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(PrivilegeIdOutOfBoundException.class)
    public ResponseEntity<List<ErrorResponse>> managePrivilegeIdOutOfBoundException(){
        var list = List.of(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()));
        logger.warn("Error response : {}, error code : {}", list,HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);    }

    @ExceptionHandler(PasswordStandardException.class)
    public ResponseEntity<List<ErrorResponse>> managePasswordStandardException(){
        var list = List.of(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()));
        logger.warn("Error response : {}, error code : {}", list,HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullRequestException.class)
    public ResponseEntity<List<ErrorResponse>> manageNullRequestException(){
        var list = List.of(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()));
        logger.warn("Error response : {}, error code : {}", list,HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotVerifiedException.class)
    public ResponseEntity<List<ErrorResponse>> manageUserNotVerifiedException(){
        var list = List.of(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()));
        logger.warn("Error response : {}, error code : {}", list,HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(list, HttpStatus.FORBIDDEN);
    }
}
