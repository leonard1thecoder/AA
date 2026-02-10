package com.retails.followers.application.executor;

import com.retails.followers.application.dto.UserFollowersRetailResponse;
import com.retails.followers.application.service.UserFollowersRetailService;
import com.utils.application.ExceptionHandlerReporter;
import com.utils.application.RequestContract;
import com.utils.application.ResponseContract;
import com.utils.application.controllerAdvice.ExecutorControllerAdvice;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;
import static com.utils.application.ExceptionHandler.returnErrorResponse;

public class UserFollowsRetailServiceExecutor {
private final Logger logger = LoggerFactory.getLogger(UserFollowsRetailServiceExecutor.class);
    @Getter
    private final static UserFollowsRetailServiceExecutor instance = new UserFollowsRetailServiceExecutor();
    private final ExecutorService executorService;



    private UserFollowsRetailServiceExecutor() {
        this.executorService = Executors.newVirtualThreadPerTaskExecutor();
    }


    public List<? extends ResponseContract> buildContactFormServiceExecutor(UserFollowersRetailService service, RequestContract request,String serviceRunner) {
        var retryTime = 3;
        var retryAttempt = 0;
        Future<List<? extends UserFollowersRetailResponse>> future = this.executorService.submit(() -> service.call(serviceRunner,request));
        while (retryAttempt < retryTime) {
        try {
            return future.get(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            var errorMessage = ExecutorControllerAdvice.setMessage("Interruption occurred while executing service : " + service + " reason " + e.getMessage());
            ExecutorControllerAdvice.setResolveIssueDetails("issue is under investigation, please try again later");
            return returnErrorResponse(false, errorMessage, "please reload the application");
        } catch (ExecutionException e) {
            return returnErrorResponse(false, ExceptionHandlerReporter.getMessage() +" "+ e.getMessage(), ExceptionHandlerReporter.getResolveIssueDetails());
        } catch (TimeoutException e) {
            retryAttempt++;
            if (retryAttempt >= retryTime) {
                ExecutorControllerAdvice.setMessage("Time out occurred  while executing service : " + service + " reason service waited 60 seconds");
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
        return returnErrorResponse(false, ExceptionHandlerReporter.getMessage(), ExceptionHandlerReporter.getResolveIssueDetails());

    }
}
