package com.retails.application.controller;

import com.retails.application.dto.*;
import com.retails.application.executor.RetailCompanyExecutor;
import com.retails.application.service.RetailCompanyService;
import com.utils.application.ResponseContract;
import com.utils.application.globalExceptions.errorResponse.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.retails.application.service.RetailCompanyService.serviceHandler;


@RestController
@RequestMapping("dev/retail/v1")
public class RetailCompanyController {
   private Logger logger = LoggerFactory.getLogger(RetailCompanyController.class);

    RetailCompanyService retailCompanyService;
    RetailCompanyExecutor serviceConcurrentExecutor;

    @Autowired
    public RetailCompanyController(@Autowired RetailCompanyService retailCompanyService) {
        this.retailCompanyService = retailCompanyService;
        this.serviceConcurrentExecutor = RetailCompanyExecutor.getInstance();
    }
    @PostMapping
    public ResponseEntity<List<? extends ResponseContract>> registerRetailCompany(@RequestBody RegisterRetailCompanyRequest request, UriComponentsBuilder uriBuilder) {
        serviceHandler = "registerRetailCompany";
        retailCompanyService.setRegisterRetailCompanyRequest(request);
        var response = serviceConcurrentExecutor.buildServiceExecutor(retailCompanyService);

        if (response.get(0) instanceof RetailCompanyResponse) {
            var uri = uriBuilder.path("dev/retail/v1/getRetailCompanyById/{id}").buildAndExpand(request.getRetailCompanyName()).toUri();
            return ResponseEntity.created(uri).body(response);
        } else {
            ErrorResponse error = (ErrorResponse) response;
            logger.warn("Error response : {}", error);
            return ResponseEntity.badRequest().body(List.of(error));
        }
    }

//    @GetMapping
//    public ResponseEntity<List<? extends ResponseContract>> displayRetailCompaniesByOwnerName(@PathVariable DisplayRetailCompaniesByOwnerNameRequest request) {
//        serviceHandler = "displayRetailCompaniesByOwnerName";
//        retailCompanyService.setDisplayRetailCompaniesByOwnerNameRequest(request);
//        var response = serviceConcurrentExecutor.buildServiceExecutor(retailCompanyService);
//        if (response.get(0) instanceof RetailCompanyResponse)
//            return ResponseEntity.ok(response);
//        else{
//            ErrorResponse error = (ErrorResponse) response;
//            logger.warn("Error response : {}", error);
//            return ResponseEntity.badRequest().body(List.of(error));
//        }
//    }

    @GetMapping("/getRetailCompanyById/{id}")
    public ResponseEntity<List<? extends ResponseContract>> displayRetailCompanyById(@PathVariable Long id) {
        serviceHandler = "displayRetailCompanyByRetailCoRegNo";
        retailCompanyService.setDisplayRetailCompanyByIdRequest(DisplayRetailCompanyByIdRequest
                .builder()
                .retailCompanyId(id)
                .build());
        var response = serviceConcurrentExecutor.buildServiceExecutor(retailCompanyService);
        if (response.get(0) instanceof RetailCompanyResponse)
            return ResponseEntity.ok(response);
        else {
            ErrorResponse error = (ErrorResponse) response;
            logger.warn("Error response : {}", error);
            return ResponseEntity.badRequest().body(List.of(error));
        }
    }

    @GetMapping("/getRetailsByName")
    public ResponseEntity<List<? extends ResponseContract>> displayRetailCompanyByName(@RequestBody DisplayRetailCompanyByNameRequest request) {
        serviceHandler = "displayRetailCompanyByName";
        retailCompanyService.setDisplayRetailCompanyByNameRequest(request);
        var response = serviceConcurrentExecutor.buildServiceExecutor(retailCompanyService);
        if (response.get(0) instanceof RetailCompanyResponse)
            return ResponseEntity.ok(response);
        else {
            ErrorResponse error = (ErrorResponse) response;
            logger.warn("Error response : {}", error);
            return ResponseEntity.badRequest().body(List.of(error));
        }
    }


    @GetMapping("/getAllRetailCompanies")
    public ResponseEntity<List<? extends ResponseContract>> displayAllRetailCompanies() {
        serviceHandler = "displayAllRetailCompanies";
        var response = serviceConcurrentExecutor.buildServiceExecutor(retailCompanyService);
        if (response.get(0) instanceof RetailCompanyResponse)
            return ResponseEntity.ok(response);
        else {
            ErrorResponse error = (ErrorResponse) response;
            logger.warn("Error response : {}", error);
            return ResponseEntity.badRequest().body(List.of(error));
        }
    }
}
