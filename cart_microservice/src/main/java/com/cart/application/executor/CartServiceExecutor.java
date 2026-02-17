package com.cart.application.executor;

import com.cart.application.service.CartService;
import com.product.application.services.CompanyProductsService;
import com.product.application.services.ProductServicesListService;
import com.utils.application.*;
import com.utils.application.controllerAdvice.ExecutorControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;

import static com.utils.application.ExceptionHandler.returnErrorResponse;

public abstract class CartServiceExecutor {
    private final Logger logger = LoggerFactory.getLogger(CartServiceExecutor.class);
    private final ExecutorService executorService;


    protected CartServiceExecutor(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public List<? extends ResponseContract> executeService(ServiceContract service, RequestContract request, String serviceRunner) {
        var retryTime = 3;
        var retryAttempt = 0;
        Future<List<? extends ResponseContract>> future;
        switch (service) {
            case CartService casteService -> {
                logger.info("Executing Product liters Service");
                future = this.executorService.submit(() -> casteService.call(serviceRunner, request));
            }
            case ProductServicesListService casteService -> {
                logger.info("Executing Product List Service");
                future = this.executorService.submit(() -> casteService.call(serviceRunner, request));
            }
            case CompanyProductsService casteService -> {
                logger.info("Executing Company Product  Service");
                future = this.executorService.submit(() -> casteService.call(serviceRunner, request));
            }
            case null, default -> future = null;
        }

        while (true) {
            try {
                assert future != null;
                return future.get(15, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                var errorMessage = ExecutorControllerAdvice.setMessage("Interruption occurred while executing service : " + service + " reason " + e.getMessage());
                ExecutorControllerAdvice.setResolveIssueDetails("issue is under investigation, please try again later");
                return returnErrorResponse(false, errorMessage, "please reload the application");
            } catch (ExecutionException e) {
                return returnErrorResponse(false, ExceptionHandlerReporter.getMessage() + " " + e.getMessage(), ExceptionHandlerReporter.getResolveIssueDetails());
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
    }
}
