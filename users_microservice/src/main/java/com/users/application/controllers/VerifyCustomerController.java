package com.users.application.controllers;


import com.users.application.dtos.FindByTokenRequest;
import com.users.application.executor.ServiceConcurrentExecutor;
import com.users.application.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/dev/api/users")
public class VerifyCustomerController {

    private final UsersService service;
    private final ServiceConcurrentExecutor serviceConcurrentExecutor;


    @Autowired
    public VerifyCustomerController( @Autowired UsersService service) {
        this.serviceConcurrentExecutor = ServiceConcurrentExecutor.getInstance();
        this.service = service;
    }

    @GetMapping("/verify")
    public String verifyCustomer(@RequestParam String token) {
        FindByTokenRequest request = new FindByTokenRequest(token);
        UsersService.setFindByTokenRequest(request);
        UsersService.setServiceHandler("getUsersById");
        var response = serviceConcurrentExecutor.buildServiceExecutor(service);

        if(response.size() == 1){
            return "redirect:https://www.example.com";
        }else{
            return null;
        }
    }


}
