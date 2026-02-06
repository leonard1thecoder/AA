package com.retails.application.executor;

import com.retails.application.service.RetailCompanyService;
import com.utils.application.Execute;
import com.utils.application.ResponseContract;
import com.utils.application.controllerAdvice.ExecutorControllerAdvice;
import com.utils.application.globalExceptions.ServiceExecutionException;
import com.utils.application.globalExceptions.ServiceInterruptedException;
import com.utils.application.globalExceptions.ServiceTimeoutException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;

import static com.utils.application.ExceptionHandler.returnErrorResponse;

import static com.utils.application.ExceptionHandlerReporter.*;
public class RetailCompanyExecutor {
    Logger logger = LoggerFactory.getLogger(RetailCompanyExecutor.class);
    @Getter
    private final static RetailCompanyExecutor instance = new RetailCompanyExecutor();
    private final ExecutorService executorService;

    private Execute service;

    private RetailCompanyExecutor() {

        int threads = Runtime.getRuntime().availableProcessors();
        this.executorService = Executors.newVirtualThreadPerTaskExecutor();
    }

    private ExecutorService setThreadName(boolean isContactForm) {
        ThreadFactory threadFactory = new ThreadFactory() {
            private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();

            @Override
            public Thread newThread(Runnable r) {
                Thread t = defaultFactory.newThread(r);

                if (isContactForm) {
                    t.setName("Contact form");
                } else {
                    t.setName("AA-" + RetailCompanyService.serviceHandler); // "AA" prefix for Alcohol Agent
                }
                return t;
            }
        };
        var threads = Runtime.getRuntime().availableProcessors();
        return Executors.newFixedThreadPool(threads, threadFactory);
    }

    public List<? extends ResponseContract> buildServiceExecutor(RetailCompanyService service) {
        var retryTime = 3;
        var retryAttempt = 0;
        Future<List<? extends ResponseContract>> future = this
                .setThreadName(false)
                .submit(service::call);

        while (retryAttempt < retryTime) {
            try {
                return future.get(60, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                var errorMessage = ExecutorControllerAdvice.setMessage("Interruption occurred while executing service : " + service + " reason " + e.getMessage());
                ExecutorControllerAdvice.setResolveIssueDetails("issue is under investigation, please try again later");
                return returnErrorResponse(false, errorMessage, getResolveIssueDetails());

            } catch (ExecutionException e) {
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());
            } catch (TimeoutException e) {
                retryAttempt++;
                if (retryAttempt >= retryTime) {
                    var errorMessage = ExecutorControllerAdvice.setMessage("Time out occurred  while executing service : " + service + " reason service waited 60 seconds");
                    ExecutorControllerAdvice.setResolveIssueDetails("please try again later");
                    return returnErrorResponse(false, errorMessage, getResolveIssueDetails());
                }


                logger.info("service has failed due to time out, retrying {}:", retryAttempt);

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return returnErrorResponse(false, getMessage(), getResolveIssueDetails());

    }


}
