package com.aa.AA.utils.executors;

import com.aa.AA.dtos.UsersRequest;
import com.aa.AA.services.UsersService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

@Component
public class UsersServiceConcurrentExecutor {

    private ExecutorService executorService;
    private List<UsersRequest>  usersRequestList;

    public UsersServiceConcurrentExecutor() {

        int threads = Runtime.getRuntime().availableProcessors();
        this.executorService = Executors.newFixedThreadPool(threads);

    }

   public  void buildServiceExecutor(UsersService serviceBuilder) {
        Future <List<UsersRequest>> future = this.executorService.submit(serviceBuilder);
       try {
           usersRequestList = future.get(15, TimeUnit.SECONDS);
       } catch (InterruptedException e) {
           throw new RuntimeException(e);
       } catch (ExecutionException e) {
           throw new RuntimeException(e);
       } catch (TimeoutException e) {
           throw new RuntimeException(e);
       }
   }

    public List<UsersRequest> provideRequestList(){
        return usersRequestList;
    }


}
