package com.users.application.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {


    @GetMapping("/")
    public String index(){
        return "/home/index";
    }
}
