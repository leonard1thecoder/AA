package com.aa.AA.utils.executors;

import com.aa.AA.dtos.UsersResponse;
import com.aa.AA.services.UsersService;
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

   public  List<UsersResponse> buildServiceExecutor() {
        Future <List<UsersResponse>> future = this.executorService.submit(this.service);
       try {
           return  future.get(15, TimeUnit.SECONDS);
       } catch (InterruptedException e) {
           throw new ServiceInterruptedException(e);
       } catch (ExecutionException e) {
           throw new ServiceExecutionException(e);
       } catch (TimeoutException e) {
           throw new ServiceTimeoutException(e);
       }
   }

    public  List<UsersResponse> buildServiceExecutor(UsersService service) {
        Future <List<UsersResponse>> future = this.executorService.submit(service);
        try {
            return  future.get(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new ServiceInterruptedException(e);
        } catch (ExecutionException e) {
            throw new ServiceExecutionException(e);
        } catch (TimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

}
