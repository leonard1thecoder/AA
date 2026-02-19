package com.payment.application.exceptions.exceptionsAdvice;

import com.payment.application.exceptions.PaymentStrategyNotFoundException;
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
public class PaymentControllerAdvice extends ExceptionHandlerReporter {
    private static final Logger logger = LoggerFactory.getLogger(PaymentControllerAdvice.class);


    @ExceptionHandler(PaymentStrategyNotFoundException.class)
    public ResponseEntity<List<ErrorResponse>> managePaymentStrategyNotFoundException() {
        var list = List.of(new ErrorResponse(getIssueDateFormatted(), getResolveIssueDetails(), getMessage(), getException()));
        logger.warn("Error response : {}, error code : {}", list, HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(list, HttpStatus.FORBIDDEN);
    }
}
