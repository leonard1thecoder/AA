package com.retails.followers.application.exceptions.controllerAdvice;

import com.retails.followers.application.exceptions.FollowingRecordNotFoundException;
import com.retails.followers.application.exceptions.RetailCompanyFollowedNotFoundException;
import com.retails.followers.application.exceptions.UnfollowingFailedStatusIs0Exception;
import com.retails.followers.application.exceptions.UserFollowingRetailNotFoundException;
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
public class UserFollowerRetailsCompanyControllerAdvice extends ExceptionHandlerReporter {

    private static final Logger logger = LoggerFactory.getLogger(UserFollowerRetailsCompanyControllerAdvice.class);

    @ExceptionHandler(UserFollowingRetailNotFoundException.class)
    public ResponseEntity<List<ErrorResponse>> manageUserFollowingRetailNotFoundException(){
        var list = List.of(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()));
        logger.warn("Error response : {}, error code : {}", list, HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(list, HttpStatus.FORBIDDEN);
    }



    @ExceptionHandler(RetailCompanyFollowedNotFoundException.class)
    public ResponseEntity<List<ErrorResponse>> manageRetailCompanyFollowedNotFoundException(){
        var list = List.of(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()));
        logger.warn("Error response : {}, error code : {}", list, HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(list, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(FollowingRecordNotFoundException.class)
    public ResponseEntity<List<ErrorResponse>> manageFollowingRecordNotFoundException(){
        var list = List.of(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()));
        logger.warn("Error response : {}, error code : {}", list, HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(list, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnfollowingFailedStatusIs0Exception.class)
    public ResponseEntity<List<ErrorResponse>> manageUnfollowingFailedStatusIs0Exception(){
        var list = List.of(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage()));
        logger.warn("Error response : {}, error code : {}", list, HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(list, HttpStatus.FORBIDDEN);
    }
}
