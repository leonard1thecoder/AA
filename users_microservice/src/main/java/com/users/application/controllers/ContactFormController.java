package com.users.application.controllers;

import com.users.application.dtos.ContactFormRequest;
import com.users.application.dtos.ContactUsResponse;
import com.users.application.entities.ContactForm;
import com.users.application.executor.ServiceConcurrentExecutor;
import com.users.application.services.ContactFormService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/contact")
@Validated
public class ContactFormController {

    private final ContactFormService service;
    private final ServiceConcurrentExecutor serviceConcurrentExecutor;
    public ContactFormController(ContactFormService service) {
        this.serviceConcurrentExecutor = ServiceConcurrentExecutor.getInstance();

        this.service = service;
    }

    @PostMapping("/submit")
    public ResponseEntity<ContactUsResponse> submitEnquiry( @RequestBody ContactFormRequest request) {

        var response = serviceConcurrentExecutor.buildContactFormServiceExecutor(service,request,false);
        if (response.equals("Enquiry successfully submitted")) {
            return ResponseEntity.ok(new ContactUsResponse("success", "Enquiry successfully submitted"));
        }else{
            return ResponseEntity.badRequest().body(new ContactUsResponse("failure", response));
        }
    }


}