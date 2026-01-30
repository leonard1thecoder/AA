package com.users.application.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {


    @GetMapping("/")
    public String index(){
        return "home/index";
    }

    @GetMapping("/sign-up")
    public String signupPage() {
        return "home/sign-up"; // looks for signup.html in src/main/resources/templates
    }

    @GetMapping("/reset")
    public String resetPage() {
        return "home/reset"; // looks for reset.html in src/main/resources/templates
    }

    @GetMapping("/forgot-password")
    public String forgotPage() {
        return "home/forgot-password"; // looks for forgot.html in src/main/resources/templates
    }

    @GetMapping("/login")
    public String loginPage() {
        return "home/login"; // looks for login.html in src/main/resources/templates
    }

}
