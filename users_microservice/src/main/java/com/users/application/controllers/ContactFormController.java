package com.users.application.controllers;

import com.users.application.dtos.ContactFormRequest;
import com.users.application.dtos.ContactUsResponse;
import com.users.application.executor.UserServiceConcurrentExecutor;
import com.users.application.services.ContactFormService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/contact")
@Validated
public class ContactFormController extends UserServiceConcurrentExecutor {

    private final ContactFormService service;
    public ContactFormController(ContactFormService service) {
        super(Executors.newVirtualThreadPerTaskExecutor());
        this.service = service;
    }

    @PostMapping("/submit")
    public ResponseEntity<ContactUsResponse> submitEnquiry( @RequestBody ContactFormRequest request) {

        var response = super.buildContactFormServiceExecutor(service,request,false);
        if (response.equals("Enquiry successfully submitted")) {
            return ResponseEntity.ok(new ContactUsResponse("success", "Enquiry successfully submitted"));
        }else{
            return ResponseEntity.badRequest().body(new ContactUsResponse("failure", response));
        }
    }


}