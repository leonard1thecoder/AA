package com.users.application.controllers;


import com.users.application.dtos.FindByTokenRequest;
import com.users.application.dtos.RollBackPasswordRequest;
import com.users.application.executor.ServiceConcurrentExecutor;
import com.users.application.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/dev/api/verify")
public class VerifyCustomerController {

    private final UsersService service;
    private final ServiceConcurrentExecutor serviceConcurrentExecutor;


    @Autowired
    public VerifyCustomerController( @Autowired UsersService service) {
        this.serviceConcurrentExecutor = ServiceConcurrentExecutor.getInstance();
        this.service = service;
    }

    @GetMapping("/verify-customer")
    public String verifyCustomer(@RequestParam String qTokq1) {
        FindByTokenRequest request = new FindByTokenRequest(qTokq1);
        UsersService.setFindByTokenRequest(request);
        UsersService.setServiceHandler("verifyUser");
        var response = serviceConcurrentExecutor.buildServiceExecutor(service);

        if(response.size() == 1){
            return "redirect:https://www.example.com";
        }else{
            return null;
        }
    }


    @GetMapping("/verify-password-update")
    public String verifyPasswordUpdate(@RequestParam String qTokq1) {
        FindByTokenRequest request = new FindByTokenRequest(qTokq1);
        UsersService.setFindByTokenRequest(request);
        UsersService.setServiceHandler("verifyUser");
        var response = serviceConcurrentExecutor.buildServiceExecutor(service);
/*
I need to create update
 */
        if(response.size() == 1){
            return "redirect:https://www.example.com";
        }else{
            return null;
        }
    }

    @GetMapping("/passwordRollBack")
    public String rollbackPassword(@RequestParam String qTokq1,@RequestParam String emailAddress){
        RollBackPasswordRequest request = new RollBackPasswordRequest(qTokq1,emailAddress);
        UsersService.setRollBackPasswordRequest(request);
        UsersService.setServiceHandler("passwordRollBack");
        var response = serviceConcurrentExecutor.buildServiceExecutor(service);
/*
I need to create update
 */
        if(response.size() == 1){
            return "redirect:https://www.example.com";
        }else{
            return null;
        }
    }


}
