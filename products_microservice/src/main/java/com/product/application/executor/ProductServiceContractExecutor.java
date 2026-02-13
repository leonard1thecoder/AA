package com.product.application.executor;

import com.product.application.services.CompanyProductsService;
import com.product.application.services.ProductServicesContract;
import com.product.application.services.ProductServicesListService;
import com.product.application.services.ProductsLitersService;
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

public class ProductServiceContractExecutor {
    private final Logger logger = LoggerFactory.getLogger(ProductServiceContractExecutor.class);
    @Getter
    private final static ProductServiceContractExecutor instance = new ProductServiceContractExecutor();
    private final ExecutorService executorService;



    private ProductServiceContractExecutor() {
        this.executorService = Executors.newVirtualThreadPerTaskExecutor();
    }


    public List<? extends ResponseContract> buildContactFormServiceExecutor(ProductServicesContract service, RequestContract request, String serviceRunner) {
        var retryTime = 3;
        var retryAttempt = 0;
        Future<List<? extends ResponseContract>> future;
        if(service instanceof ProductsLitersService casteService) {
           logger.info("Executing Product liters Service");
            future = this.executorService.submit(() -> casteService.call(serviceRunner, request));
        }else  if(service instanceof ProductServicesListService casteService) {
            logger.info("Executing Product List Service");
            future = this.executorService.submit(() -> casteService.call(serviceRunner, request));
        }else if(service instanceof CompanyProductsService casteService){
            logger.info("Executing Company Product  Service");
            future = this.executorService.submit(() -> casteService.call(serviceRunner,request));
        } else
            future = null;

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
