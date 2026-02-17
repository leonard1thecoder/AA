package com.product.application.exceptions.advice;

import com.product.application.exceptions.*;
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
public class ProductControllerAdvice extends ExceptionHandlerReporter {
    private static final Logger logger = LoggerFactory.getLogger(ProductControllerAdvice.class);

    @ExceptionHandler(ProductLiterNotFoundException.class)
    public ResponseEntity<List<ErrorResponse>> manageProductLiterNotFoundException(){
        var list = List.of(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage(),getException()));
        logger.warn("Error response : {}, error code : {}", list, HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(list, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(QuantityNotEnoughException.class)
    public ResponseEntity<List<ErrorResponse>> manageQuantityNotEnoughException(){
        var list = List.of(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage(),getException()));
        logger.warn("Error response : {}, error code : {}", list, HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(list, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ProductNotAvailableException.class)
    public ResponseEntity<List<ErrorResponse>> manageProductNotAvailableException(){
        var list = List.of(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage(),getException()));
        logger.warn("Error response : {}, error code : {}", list, HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(list, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotPrivilegedToSentRequestException.class)
    public ResponseEntity<List<ErrorResponse>> manageNotPrivilegedToSentRequestException(){
        var list = List.of(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage(),getException()));
        logger.warn("Error response : {}, error code : {}", list, HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(list, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(CompanyProductListNotFoundException.class)
    public ResponseEntity<List<ErrorResponse>> manageCompanyProductListNotFoundException(){
        var list = List.of(new ErrorResponse(getIssueDateFormatted(),getResolveIssueDetails(), getMessage(),getException()));
        logger.warn("Error response : {}, error code : {}", list, HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(list, HttpStatus.FORBIDDEN);
    }

}
