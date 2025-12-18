package com.utils.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;

import static com.utils.application.ExceptionHandlerReporter.formatDateTime;


public class ExceptionHandler {
   private static final Logger   logger = LoggerFactory.getLogger(ExceptionHandler.class);

    public static RuntimeException throwExceptionAndReport(RuntimeException ex,String errorMessage,String resolveIssueDetails){
        try{
            ExceptionHandlerReporter.setMessage(errorMessage);
            ExceptionHandlerReporter.setResolveIssueDetails(resolveIssueDetails);
            throw ex;
        }catch (RuntimeException reportException){
            logger.warn("Error thrown by exception : {}\n Time thrown : {}\n Trace exception :{}", ex,formatDateTime(LocalDateTime.now()) , Arrays.stream(reportException.getStackTrace()).toList());
            return reportException;
        }
    }
}
