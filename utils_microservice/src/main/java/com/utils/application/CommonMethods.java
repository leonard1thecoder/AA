package com.utils.application;

import com.utils.application.controllerAdvice.ExecutorControllerAdvice;
import com.utils.application.globalExceptions.ServiceTimeoutException;
import com.utils.application.globalExceptions.errorResponse.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static com.utils.application.ExceptionHandler.returnErrorResponse;
import static com.utils.application.ExceptionHandler.throwExceptionAndReport;

public class CommonMethods {
private static final Logger logger= LoggerFactory.getLogger(CommonMethods.class);
    public static String formatDateTime(LocalDateTime issueDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return issueDate.format(formatter);
    }

    public static List<ErrorResponse> retryServiceTimeout(int retryAttempt, int retryTime, ServiceContract service){
        retryAttempt++;
        if (retryAttempt >= retryTime) {
            try {
                ExecutorControllerAdvice.setMessage("Time out occurred  while executing service : " + service + " reason service waited 60 seconds");
                ExecutorControllerAdvice.setResolveIssueDetails("please try again later");
                throw throwExceptionAndReport(new ServiceTimeoutException(ExceptionHandlerReporter.getMessage()), ExceptionHandlerReporter.getMessage(), ExceptionHandlerReporter.getResolveIssueDetails());
            }catch(RuntimeException ee){
                return returnErrorResponse(ExceptionHandlerReporter.getMessage(), ExceptionHandlerReporter.getResolveIssueDetails(), ee);
            }
        }


        logger.info("service has failed due to time out, retrying {}:", retryAttempt);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            logger.info("During service time out, sleep method interrupted while sleeping 5 seconds : {}",ex.getMessage());

        }
        return null;
    }

}
