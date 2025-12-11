package com.users.application.executor;



import com.users.application.exceptions.UserNotFoundException;
import com.users.application.exceptions.UsersPasswordIncorrectException;
import com.utils.application.Execute;
import com.utils.application.ResponseContract;
import com.utils.application.controllerAdvice.ExecutorControllerAdvice;
import com.utils.application.globalExceptions.ServiceExecutionException;
import com.utils.application.globalExceptions.ServiceInterruptedException;
import com.utils.application.globalExceptions.ServiceTimeoutException;
import com.utils.application.globalExceptions.errorResponse.ErrorResponse;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.*;

import static com.utils.application.ExceptionHandler.throwExceptionAndReport;
import static com.utils.application.ExceptionHandlerReporter.*;

public class ServiceConcurrentExecutor {

    @Getter
    private final static ServiceConcurrentExecutor instance = new ServiceConcurrentExecutor();
    private ExecutorService executorService;

    private Execute service;

    private ServiceConcurrentExecutor() {
        int threads = Runtime.getRuntime().availableProcessors();
        this.executorService = Executors.newFixedThreadPool(threads);
    }

    public List<? extends ResponseContract> buildServiceExecutor() {
        Future<List<? extends ResponseContract>> future = this.executorService.submit(this.service);
        try {
            return future.get(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            var errorMessage = ExecutorControllerAdvice.setMessage("Interruption occurred while executing service : " + service +" reason "+ e.getMessage());
            ExecutorControllerAdvice.setResolveIssueDetails("issue is under investigation, please try again later");
            throw new ServiceInterruptedException(errorMessage);
        } catch (ExecutionException e) {
            var errorMessage = ExecutorControllerAdvice.setMessage("Execution failed while executing service : " + service +" reason "+ e.getMessage());
            ExecutorControllerAdvice.setResolveIssueDetails("issue is under investigation, please try again later");
            throw new ServiceExecutionException(errorMessage);
        } catch (TimeoutException e) {
            var errorMessage = ExecutorControllerAdvice.setMessage("Time out occurred  while executing service : " + service +" reason service waited 15 seconds");
            ExecutorControllerAdvice.setResolveIssueDetails("please try again later");
            throw new ServiceTimeoutException(errorMessage);
        }
    }

    public List<? extends ResponseContract> buildServiceExecutor(Execute service) {
        Future<List<? extends ResponseContract>> future = this.executorService.submit(service);
        try {
            return future.get(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            var errorMessage = ExecutorControllerAdvice.setMessage("Interruption occurred while executing service : " + service +" reason "+ e.getMessage());
            ExecutorControllerAdvice.setResolveIssueDetails("issue is under investigation, please try again later");
            if(e.getMessage().contains("UserNotFoundException"))
                throw throwExceptionAndReport(new UserNotFoundException(errorMessage), errorMessage, getResolveIssueDetails());
            else
            return null;
        } catch (ExecutionException e) {
            if(e.getMessage().contains("UserNotFoundException"))
                throw throwExceptionAndReport(new UserNotFoundException(getMessage()), getMessage(), getResolveIssueDetails());
            else if(e.getMessage().contains("UsersPasswordIncorrectException"))
                throw throwExceptionAndReport(new UsersPasswordIncorrectException(getMessage()), getMessage(), getResolveIssueDetails());
            else
                return null;
        } catch (TimeoutException e) {
            var errorMessage = ExecutorControllerAdvice.setMessage("Time out occurred  while executing service : " + service +" reason service waited 15 seconds");
            ExecutorControllerAdvice.setResolveIssueDetails("please try again later");
            throw new ServiceTimeoutException(errorMessage);
        }
    }

}
