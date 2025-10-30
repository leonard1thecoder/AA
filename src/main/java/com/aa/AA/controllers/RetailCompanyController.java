package com.aa.AA.controllers;

import com.aa.AA.dtos.DisplayRetailCompaniesByOwnerNameRequest;
import com.aa.AA.dtos.DisplayRetailCompanyByNameRequest;
import com.aa.AA.dtos.DisplayRetailCompanyByRetailCoRegNoRequest;
import com.aa.AA.dtos.RegisterRetailCompanyRequest;
import com.aa.AA.services.RetailCompanyService;
import com.aa.AA.utils.executors.ResponseContract;
import com.aa.AA.utils.executors.ServiceConcurrentExecutor;
import com.aa.AA.utils.mappers.RetailCompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("dev/retail/v1")
public class RetailCompanyController {

    RetailCompanyMapper retailCompanyMapper;
    RetailCompanyService retailCompanyService;
    ServiceConcurrentExecutor serviceConcurrentExecutor;

    @Autowired
    public RetailCompanyController(@Autowired RetailCompanyMapper retailCompanyMapper,@Autowired RetailCompanyService retailCompanyService,@Autowired ServiceConcurrentExecutor serviceConcurrentExecutor) {
        this.retailCompanyMapper = retailCompanyMapper;
        this.retailCompanyService = retailCompanyService;
        this.serviceConcurrentExecutor = serviceConcurrentExecutor;
    }

    @PostMapping
    public ResponseEntity<List<? extends ResponseContract>> registerRetailCompany(@RequestBody RegisterRetailCompanyRequest request){
        RetailCompanyService.serviceHandler = "registerRetailCompany";
        retailCompanyService.setRegisterLiquorStoreRequest(request);
        var response = serviceConcurrentExecutor.buildServiceExecutor(retailCompanyService);
        return  ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<? extends ResponseContract>> displayRetailCompaniesByOwnerName(@RequestBody DisplayRetailCompaniesByOwnerNameRequest request){
        RetailCompanyService.serviceHandler = "displayRetailCompaniesByOwnerName";
        retailCompanyService.setDisplayRetailCompaniesByOwnerNameRequest(request);
        var response = serviceConcurrentExecutor.buildServiceExecutor(retailCompanyService);
        return  ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<? extends ResponseContract>> displayRetailCompanyByRetailCoRegNo(@RequestBody DisplayRetailCompanyByRetailCoRegNoRequest request){
        RetailCompanyService.serviceHandler = "displayRetailCompanyByRetailCoRegNo";
        retailCompanyService.setDisplayRetailCompanyByRetailCoRegNoRequest(request);
        var response = serviceConcurrentExecutor.buildServiceExecutor(retailCompanyService);
        return  ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<? extends ResponseContract>> displayRetailCompanyByName(@RequestBody DisplayRetailCompanyByNameRequest request){
        RetailCompanyService.serviceHandler = "displayRetailCompanyByName";
        retailCompanyService.setDisplayRetailCompanyByNameRequest(request);
        var response = serviceConcurrentExecutor.buildServiceExecutor(retailCompanyService);
        return  ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<List<? extends ResponseContract>> displayAllRetailCompanies(@RequestBody DisplayRetailCompanyByNameRequest request){
        RetailCompanyService.serviceHandler = "displayAllRetailCompanies";
        var response = serviceConcurrentExecutor.buildServiceExecutor();
        return  ResponseEntity.ok(response);
    }
}
