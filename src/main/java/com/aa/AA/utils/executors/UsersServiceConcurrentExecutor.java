package com.aa.AA.utils.executors;

import com.aa.AA.dtos.UsersRequest;
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

   public  List<UsersRequest> buildServiceExecutor() {
        Future <List<UsersRequest>> future = this.executorService.submit(this.service);
       try {
           return  future.get(15, TimeUnit.SECONDS);
       } catch (InterruptedException e) {
           throw new RuntimeException(e);
       } catch (ExecutionException e) {
           throw new RuntimeException(e);
       } catch (TimeoutException e) {
           throw new RuntimeException(e);
       }
   }

    public  List<UsersRequest> buildServiceExecutor(UsersService service) {
        Future <List<UsersRequest>> future = this.executorService.submit(service);
        try {
            return  future.get(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

}
