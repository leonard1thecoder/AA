package com.utils.application;

import com.utils.application.globalExceptions.errorResponse.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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

    public static List<ErrorResponse> returnErrorResponse(boolean logging,RuntimeException ex, String errorMessage, String resolveIssueDetails){
        try{
            ExceptionHandlerReporter.setMessage(errorMessage);
            ExceptionHandlerReporter.setResolveIssueDetails(resolveIssueDetails);
            throw ex;
        }catch (RuntimeException reportException){
            if(logging)
                logger.warn("Error thrown by exception : {}\n Time thrown : {}\n Trace exception :{}", ex,formatDateTime(LocalDateTime.now()) , Arrays.stream(reportException.getStackTrace()).toList());

            return List.of(new ErrorResponse(errorMessage,formatDateTime(),resolveIssueDetails));
        }
    }

    public static List<ErrorResponse> returnErrorResponse(boolean logging, String errorMessage, String resolveIssueDetails){

            ExceptionHandlerReporter.setMessage(errorMessage);
            ExceptionHandlerReporter.setResolveIssueDetails(resolveIssueDetails);

            if(logging)
                logger.warn("Exception trhown by controller advicer exception details ");

            return List.of(new ErrorResponse(errorMessage,formatDateTime(),resolveIssueDetails));

    }
}