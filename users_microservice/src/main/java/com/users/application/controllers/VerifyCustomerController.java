package com.users.application.controllers;


import com.users.application.dtos.FindByTokenRequest;
import com.users.application.dtos.UsersResponse;
import com.users.application.executor.UserServiceConcurrentExecutor;
import com.users.application.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.Executors;

@Controller
@RequestMapping("/dev/api/verify")
public class VerifyCustomerController extends UserServiceConcurrentExecutor{

    private final UsersService service;

    @Autowired
    public VerifyCustomerController( @Autowired UsersService service) {
       super(Executors.newVirtualThreadPerTaskExecutor());
       this.service = service;
    }

    @GetMapping("/verify-customer")
    public String verifyCustomer(@RequestParam String qTokq1) {
        var response = super.executeUserService(service,"verifyUser",new FindByTokenRequest(qTokq1)).getFirst();

        if (response instanceof UsersResponse) {
            return "redirect:https://www.example.com";
        }else{
            return null;
        }
    }


    @GetMapping("/verify-password-update")
    public String verifyPasswordUpdate(@RequestParam String qTokq1) {
        var response = super.executeUserService(service,"verifyUser",new FindByTokenRequest(qTokq1)).getFirst();

        if (response instanceof UsersResponse users) {
            return "redirect:/reset?emailAddess="+users.getUsersEmailAddress();
        } else {
            return "redirect:/login";
        }
    }




}
