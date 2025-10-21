package com.aa.AA.utils.executors;

import com.aa.AA.dtos.UsersResponse;
import com.aa.AA.services.UsersService;
import com.aa.AA.utils.exceptions.ServiceExecutionException;
import com.aa.AA.utils.exceptions.ServiceInterruptedException;
import com.aa.AA.utils.exceptions.ServiceTimeoutException;
import com.aa.AA.utils.exceptions.controllerAdvices.UsersControllerAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

@Component
public class UsersServiceConcurrentExecutor {

    private ExecutorService executorService;

    private UsersService service;

    @Autowired
    public UsersServiceConcurrentExecutor(@Autowired UsersService service) {
        this.service = service;
        int threads = Runtime.getRuntime().availableProcessors();
        this.executorService = Executors.newFixedThreadPool(threads);
    }

    public List<UsersResponse> buildServiceExecutor() {
        Future<List<UsersResponse>> future = this.executorService.submit(this.service);
        try {
            return future.get(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            var errorMessage = UsersControllerAdvice.setMessage("Interruption occurred while executing service : " + service +" reason "+ e.getMessage());
            UsersControllerAdvice.setResolveIssueDetails("issue is under investigation, please try again later");
            throw new ServiceInterruptedException(errorMessage);
        } catch (ExecutionException e) {
            var errorMessage = UsersControllerAdvice.setMessage("Execution failed while executing service : " + service +" reason "+ e.getMessage());
            UsersControllerAdvice.setResolveIssueDetails("issue is under investigation, please try again later");
            throw new ServiceExecutionException(errorMessage);
        } catch (TimeoutException e) {
            var errorMessage = UsersControllerAdvice.setMessage("Time out occurred  while executing service : " + service +" reason service waited 15 seconds");
            UsersControllerAdvice.setResolveIssueDetails("please try again later");
            throw new ServiceTimeoutException(e.getMessage());
        }
    }

    public List<UsersResponse> buildServiceExecutor(UsersService service) {
        Future<List<UsersResponse>> future = this.executorService.submit(service);
        try {
            return future.get(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            var errorMessage = UsersControllerAdvice.setMessage("Interruption occurred while executing service : " + service +" reason "+ e.getMessage());
            UsersControllerAdvice.setResolveIssueDetails("issue is under investigation, please try again later");
            throw new ServiceInterruptedException(errorMessage);
        } catch (ExecutionException e) {
            var errorMessage = UsersControllerAdvice.setMessage("Execution failed while executing service : " + service +" reason "+ e.getMessage());
            UsersControllerAdvice.setResolveIssueDetails("issue is under investigation, please try again later");
            throw new ServiceExecutionException(errorMessage);
        } catch (TimeoutException e) {
            var errorMessage = UsersControllerAdvice.setMessage("Time out occurred  while executing service : " + service +" reason service waited 15 seconds");
            UsersControllerAdvice.setResolveIssueDetails("please try again later");
            throw new ServiceTimeoutException(e.getMessage());
        }
    }

}
