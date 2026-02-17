package com.cart.application.exceptions.advice;

import com.cart.application.exceptions.CartNotAvailableException;
import com.cart.application.exceptions.ProductNotInCartException;
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
public class CartControllerAdvice extends ExceptionHandlerReporter {
    Logger logger = LoggerFactory.getLogger(CartControllerAdvice.class);

    @ExceptionHandler(CartNotAvailableException.class)
    public ResponseEntity<List<ErrorResponse>> manageCartNotAvailableException() {
        var list = List.of(new ErrorResponse(getIssueDateFormatted(), getResolveIssueDetails(), getMessage(), getException()));
        logger.warn("Error response : {}, error code : {}", list, HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(list, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ProductNotInCartException.class)
    public ResponseEntity<List<ErrorResponse>> manageProductNotInCartException() {
        var list = List.of(new ErrorResponse(getIssueDateFormatted(), getResolveIssueDetails(), getMessage(), getException()));
        logger.warn("Error response : {}, error code : {}", list, HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(list, HttpStatus.FORBIDDEN);
    }

}
