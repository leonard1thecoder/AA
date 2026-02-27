package com.users.application.executor;


import com.privileges.application.service.PrivilegesService;
import com.users.application.dtos.ContactFormRequest;
import com.users.application.services.ContactFormService;
import com.users.application.services.UsersService;
import com.utils.application.RequestContract;
import com.utils.application.ResponseContract;
import com.utils.application.controllerAdvice.ExecutorControllerAdvice;
import com.utils.application.globalExceptions.ServiceInterruptedException;
import com.utils.application.globalExceptions.ServiceTimeoutException;

import com.utils.application.globalExceptions.errorResponse.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;

import static com.utils.application.CommonMethods.retryServiceTimeout;
import static com.utils.application.ExceptionHandler.returnErrorResponse;
import static com.utils.application.ExceptionHandlerReporter.*;

public abstract class UserServiceConcurrentExecutor {
    Logger logger = LoggerFactory.getLogger(UserServiceConcurrentExecutor.class);

    private final ExecutorService executorService;

    protected UserServiceConcurrentExecutor(ExecutorService executorService) {
        this.executorService =executorService;
    }

    protected String buildContactFormServiceExecutor(ContactFormService service, ContactFormRequest request, boolean isList) {

        Future<String> future = this.executorService.submit(() -> service.save(request));
        try {
            return future.get(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            var errorMessage = ExecutorControllerAdvice.setMessage("Interruption occurred while executing service : " + service + " reason " + e.getMessage());
            ExecutorControllerAdvice.setResolveIssueDetails("issue is under investigation, please try again later");
            throw new ServiceInterruptedException(errorMessage);
        } catch (ExecutionException e) {
return "error " + e.getMessage();
        } catch (TimeoutException e) {
            var errorMessage = ExecutorControllerAdvice.setMessage("Time out occurred  while executing service : " + service + " reason service waited 15 seconds");
            ExecutorControllerAdvice.setResolveIssueDetails("please try again later");
            throw new ServiceTimeoutException(errorMessage);
        }
    }

    protected List<? extends ResponseContract> buildServiceExecutor(PrivilegesService repo, String privilegeServiceManager) {
        Future<List<? extends ResponseContract>> future = this
                .executorService.submit(() -> repo.call(privilegeServiceManager));
        try {
            return future.get(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return returnErrorResponse( "Error occurred while executing privilege service, service was interrupted", "Contact AA Administrator",e);

        } catch (ExecutionException e) {
            return returnErrorResponse( "Error occurred while executing privilege service trace :  " + e.getStackTrace(), "Contact AA Administrator",e);

        } catch (TimeoutException e) {
            return returnErrorResponse( "Timeout Error occurred while executing privilege service :  ", "Contact AA Administrator",e);

        }

    }

    protected List<? extends ResponseContract> executeUserService(UsersService service, String serviceRunner, RequestContract request) {
        var retryTime = 3;
        var retryAttempt = 0;
        Future<List<? extends ResponseContract>> future = this
                .executorService
                .submit(()->service.call(serviceRunner,request));

        while (true) {
            try {
                return future.get(60, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                var errorMessage = ExecutorControllerAdvice.setMessage("Interruption occurred while executing service : " + service + " reason " + e.getMessage());
                ExecutorControllerAdvice.setResolveIssueDetails("issue is under investigation, please try again later");
                return returnErrorResponse( errorMessage, getResolveIssueDetails(),e);
            } catch (ExecutionException e) {
                return returnErrorResponse( getMessage() + e.getMessage(), getResolveIssueDetails(),e);
            } catch (TimeoutException e) {
                List<ErrorResponse> errorResponses = retryServiceTimeout(retryAttempt, retryTime, service);
               if(errorResponses != null)
                return errorResponses;
            }
        }


    }

}
