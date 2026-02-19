package com.payment.application.executor;

import com.cart.application.executor.CartServiceExecutor;
import com.cart.application.service.CartService;
import com.product.application.services.CompanyProductsService;
import com.product.application.services.ProductServicesListService;
import com.utils.application.ExceptionHandlerReporter;
import com.utils.application.RequestContract;
import com.utils.application.ResponseContract;
import com.utils.application.ServiceContract;
import com.utils.application.controllerAdvice.ExecutorControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;

import static com.utils.application.CommonMethods.retryServiceTimeout;
import static com.utils.application.ExceptionHandler.returnErrorResponse;

public abstract class PaymentServiceExecutor {
    private final Logger logger = LoggerFactory.getLogger(PaymentServiceExecutor.class);
    private final ExecutorService executorService;


    protected PaymentServiceExecutor(ExecutorService executorService) {
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
                return returnErrorResponse( errorMessage, "please reload the application",e);
            } catch (ExecutionException e) {
                return returnErrorResponse( ExceptionHandlerReporter.getMessage() + " " + e.getMessage(), ExceptionHandlerReporter.getResolveIssueDetails(),e.getCause());
            } catch (TimeoutException e) {
               var errorResponse = retryServiceTimeout(retryAttempt,retryTime,service);
                if( errorResponse != null){
                   return errorResponse;
               }
            }
        }
    }
}
