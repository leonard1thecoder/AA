package com.users.application.executor;


import com.users.application.dtos.ContactFormRequest;
import com.users.application.dtos.ContactUsResponse;
import com.users.application.dtos.UsersResponse;
import com.users.application.entities.ContactForm;
import com.users.application.exceptions.*;
import com.users.application.services.ContactFormService;
import com.users.application.services.UsersService;
import com.utils.application.Execute;
import com.utils.application.ResponseContract;
import com.utils.application.controllerAdvice.ExecutorControllerAdvice;
import com.utils.application.globalExceptions.JwtExpiredOnSessionException;
import com.utils.application.globalExceptions.ServiceExecutionException;
import com.utils.application.globalExceptions.ServiceInterruptedException;
import com.utils.application.globalExceptions.ServiceTimeoutException;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.List;
import java.util.concurrent.*;

import static com.utils.application.ExceptionHandler.returnErrorResponse;
import static com.utils.application.ExceptionHandler.throwExceptionAndReport;
import static com.utils.application.ExceptionHandlerReporter.*;

public class ServiceConcurrentExecutor {
    Logger logger = LoggerFactory.getLogger(ServiceConcurrentExecutor.class);
    @Getter
    private final static ServiceConcurrentExecutor instance = new ServiceConcurrentExecutor();
    private final ExecutorService executorService;

    private Execute service;

    private ServiceConcurrentExecutor() {

        int threads = Runtime.getRuntime().availableProcessors();
this.executorService = Executors.newVirtualThreadPerTaskExecutor();    }

    private ExecutorService setThreadName(boolean isContactForm) {
        ThreadFactory threadFactory = new ThreadFactory() {
            private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();

            @Override
            public Thread newThread(Runnable r) {
                Thread t = defaultFactory.newThread(r);

                if(isContactForm){
 t.setName("Contact form");
              }else {
                    t.setName("AA-" + UsersService.serviceHandler); // "AA" prefix for Alcohol Agent
                }
                return t;
            }
        };
        var threads = Runtime.getRuntime().availableProcessors();
        return Executors.newFixedThreadPool(threads, threadFactory);
    }

    public List<? extends ResponseContract> buildServiceExecutor() {

        Future<List<? extends ResponseContract>> future = this.executorService.submit(this.service);
        try {
            return future.get(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            var errorMessage = ExecutorControllerAdvice.setMessage("Interruption occurred while executing service : " + service + " reason " + e.getMessage());
            ExecutorControllerAdvice.setResolveIssueDetails("issue is under investigation, please try again later");
            throw new ServiceInterruptedException(errorMessage);
        } catch (ExecutionException e) {
            var errorMessage = ExecutorControllerAdvice.setMessage("Execution failed while executing service : " + service + " reason " + e.getMessage());
            ExecutorControllerAdvice.setResolveIssueDetails("issue is under investigation, please try again later");
            throw new ServiceExecutionException(errorMessage);
        } catch (TimeoutException e) {
            var errorMessage = ExecutorControllerAdvice.setMessage("Time out occurred  while executing service : " + service + " reason service waited 15 seconds");
            ExecutorControllerAdvice.setResolveIssueDetails("please try again later");
            throw new ServiceTimeoutException(errorMessage);
        }
    }

    public String buildContactFormServiceExecutor(ContactFormService service, ContactFormRequest request, boolean  isList) {

        Future<String> future = this.setThreadName(true).submit(() -> service.save(request));
        try {
            return future.get(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            var errorMessage = ExecutorControllerAdvice.setMessage("Interruption occurred while executing service : " + service + " reason " + e.getMessage());
            ExecutorControllerAdvice.setResolveIssueDetails("issue is under investigation, please try again later");
            throw new ServiceInterruptedException(errorMessage);
        } catch (ExecutionException e) {
                throw throwExceptionAndReport(new InvalidArgumentException(getMessage()), getMessage(), getResolveIssueDetails());
        } catch (TimeoutException e) {
            var errorMessage = ExecutorControllerAdvice.setMessage("Time out occurred  while executing service : " + service + " reason service waited 15 seconds");
            ExecutorControllerAdvice.setResolveIssueDetails("please try again later");
            throw new ServiceTimeoutException(errorMessage);
        }
    }

    public List<? extends ResponseContract> buildServiceExecutor(UsersService service) {
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
                if (e.getMessage().contains("UserNotFoundException"))
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("UsersPasswordIncorrectException"))
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("CachedUsersPasswordChangedException"))
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("NullRequestException"))
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("UsersExistsException"))
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("UserNotVerifiedException"))
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("UserEmailDoesNotExistException"))
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("PasswordMisMatchException"))
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("JwtExpiredOnSessionException"))
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("InvalidUserStatusException"))
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("VerifyEmailAddressException"))
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("UserAgeException"))
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("CellphoneNuException"))
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("IdentityNoIsEmptyException"))
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("IdentityNuContainsIncorrectValuesException"))
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("IdentityNuException"))
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("UserDateTimeException"))
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("VerificationTokenIncorrectException"))
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("ResetPasswordSessionException"))
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());

			e.printStackTrace();
                return returnErrorResponse(false, "Unknown Exception occurred trace :  " + e.getStackTrace(), "Contact AA Administrator");
            } catch (TimeoutException e) {
                retryAttempt++;
                if (retryAttempt >= retryTime) {
                    var errorMessage = ExecutorControllerAdvice.setMessage("Time out occurred  while executing service : " + service + " reason service waited 60 seconds");
                    ExecutorControllerAdvice.setResolveIssueDetails("please try again later");
                    return returnErrorResponse(false, getMessage(), getResolveIssueDetails());
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
