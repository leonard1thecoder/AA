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
        this.executorService = Executors.newFixedThreadPool(threads);
    }

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
        var user_id = service.call().get(0).getId();
        Future<List<? extends ResponseContract>> future = this.setThreadName(false).submit(() -> {
          if(!UsersService.serviceHandler.equals("getAllUsers"))
            MDC.put("taskName", "User_id:"+user_id);
          else
              MDC.put("taskName", "ADMIN");

            try {
                return service.call();
            } finally {
                MDC.remove("taskName");
            }
        });

        while (retryAttempt < retryTime) {
            try {
                return future.get(60, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                var errorMessage = ExecutorControllerAdvice.setMessage("Interruption occurred while executing service : " + service + " reason " + e.getMessage());
                ExecutorControllerAdvice.setResolveIssueDetails("issue is under investigation, please try again later");
                throw throwExceptionAndReport(new ConcurrentExecutionException(errorMessage), errorMessage, getResolveIssueDetails());

            } catch (ExecutionException e) {
                if (e.getMessage().contains("UserNotFoundException"))
                    throw throwExceptionAndReport(new UserNotFoundException(getMessage()), getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("UsersPasswordIncorrectException"))
                    throw throwExceptionAndReport(new UsersPasswordIncorrectException(getMessage()), getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("CachedUsersPasswordChangedException"))
                    throw throwExceptionAndReport(new UsersPasswordIncorrectException(getMessage()), getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("NullRequestException"))
                    throw throwExceptionAndReport(new NullRequestException(getMessage()), getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("UsersExistsException"))
                    throw throwExceptionAndReport(new UsersExistsException(getMessage()), getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("UserNotVerifiedException"))
                    throw throwExceptionAndReport(new UserNotVerifiedException(getMessage()), getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("UserEmailDoesNotExistException"))
                    throw throwExceptionAndReport(new UserEmailDoesNotExistException(getMessage()), getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("PasswordMisMatchException"))
                    throw throwExceptionAndReport(new PasswordMisMatchException(getMessage()), getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("JwtExpiredOnSessionException"))
                    throw throwExceptionAndReport(new JwtExpiredOnSessionException(getMessage()), getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("InvalidUserStatusException"))
                    throw throwExceptionAndReport(new InvalidUserStatusException(getMessage()), getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("VerifyEmailAddressException"))
                    throw throwExceptionAndReport(new VerifyEmailAddressException(getMessage()), getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("UserAgeException"))
                    throw throwExceptionAndReport(new UserAgeException(getMessage()), getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("CellphoneNuException"))
                    throw throwExceptionAndReport(new CellphoneNuException(getMessage()), getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("IdentityNoIsEmptyException"))
                    throw throwExceptionAndReport(new IdentityNoIsEmptyException(getMessage()), getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("IdentityNuContainsIncorrectValuesException"))
                    throw throwExceptionAndReport(new IdentityNuContainsIncorrectValuesException(getMessage()), getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("IdentityNuException"))
                    throw throwExceptionAndReport(new IdentityNuException(getMessage()), getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("UserDateTimeException"))
                    throw throwExceptionAndReport(new UserDateTimeException(getMessage()), getMessage(), getResolveIssueDetails());
                else if (e.getMessage().contains("VerificationTokenIncorrectException"))
                    throw throwExceptionAndReport(new VerificationTokenIncorrectException(getMessage()), getMessage(), getResolveIssueDetails());


                throw throwExceptionAndReport(new ConcurrentExecutionException("Unknown Exception occurred trace :  " + e.getMessage()), "Unknown Exception occurred trace :  " + e.getMessage(), "Contact AA Administrator");
            } catch (TimeoutException e) {
                retryAttempt++;
                if (retryAttempt >= retryTime) {
                    var errorMessage = ExecutorControllerAdvice.setMessage("Time out occurred  while executing service : " + service + " reason service waited 60 seconds");
                    ExecutorControllerAdvice.setResolveIssueDetails("please try again later");
                    throw throwExceptionAndReport(new ServiceTimeoutException(errorMessage), getMessage(), getResolveIssueDetails());
                }


                logger.info("service has failed due to time out, retrying {}:", retryAttempt);

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        throw throwExceptionAndReport(new ServiceTimeoutException(getMessage()), getMessage(), getResolveIssueDetails());

    }

}
