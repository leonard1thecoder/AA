package com.utils.application;

import com.utils.application.controllerAdvice.ExecutorControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.utils.application.ExceptionHandler.returnErrorResponse;

public class CommonMethods {
private static final Logger logger= LoggerFactory.getLogger(CommonMethods.class);
    public static String formatDateTime(LocalDateTime issueDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return issueDate.format(formatter);
    }

    public static List<? extends ResponseContract> returnServiceResults(Future<List<? extends ResponseContract>> futureResults,byte retryAttempt,byte retryTimes, String serviceName) {
        while (true) {
            try {
                return futureResults.get(15, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                var errorMessage = ExecutorControllerAdvice.setMessage("Interruption occurred while executing service : " + serviceName + " reason " + e.getMessage());
                ExecutorControllerAdvice.setResolveIssueDetails("issue is under investigation, please try again later");
                return returnErrorResponse(false, errorMessage, "please reload the application");
            } catch (ExecutionException e) {
                return returnErrorResponse(false, ExceptionHandlerReporter.getMessage() + " " + e.getMessage(), ExceptionHandlerReporter.getResolveIssueDetails());
            } catch (TimeoutException e) {
                retryAttempt++;
                if (retryAttempt >= retryTimes) {
                    ExecutorControllerAdvice.setMessage("Time out occurred  while executing service : " + serviceName + " reason service waited 60 seconds");
                    ExecutorControllerAdvice.setResolveIssueDetails("please try again later");
                    return returnErrorResponse(false, ExceptionHandlerReporter.getMessage(), ExceptionHandlerReporter.getResolveIssueDetails());
                }


                logger.info("service has failed due to time out, retrying {}:", retryAttempt);

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
