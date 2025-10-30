package com.aa.AA.controllers;

import com.aa.AA.dtos.*;
import com.aa.AA.services.UsersService;
import com.aa.AA.utils.config.CachingConfig;
import com.aa.AA.utils.executors.ResponseContract;
import com.aa.AA.utils.executors.ServiceConcurrentExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dev/api/users/")
public class UsersController {

    private UsersService service;
    private ServiceConcurrentExecutor serviceConcurrentExecutor;
    private CachingConfig config;

    @Autowired

    public UsersController(@Autowired CachingConfig config, @Autowired UsersService service, @Autowired ServiceConcurrentExecutor serviceConcurrentExecutor) {
        this.serviceConcurrentExecutor = serviceConcurrentExecutor;
        this.service = service;
        this.config = config;
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<? extends ResponseContract>> getAllUsers() {
        UsersService.setServiceHandler("getAllUsers");
        var list = this.serviceConcurrentExecutor.buildServiceExecutor();
        return ResponseEntity.ok(list);
    }


    @GetMapping("/getAllUsers/{id}")
    public ResponseEntity<List<? extends ResponseContract>> getUserById(@PathVariable Long id) {
        UsersService.setServiceHandler("getUsersById");
        service.setFindByIdRequest(new FindByIdRequest(id));
        var list = this.serviceConcurrentExecutor.buildServiceExecutor(service);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/getUsersByFullName")
    public ResponseEntity<List<? extends ResponseContract>> getUserByFullName(@PathVariable UsersFullNameRequest request) {
        UsersService.setServiceHandler("getUsersByFullName");
        service.setUsersFullNameRequest(request);
        var list = this.serviceConcurrentExecutor.buildServiceExecutor(service);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/findUserByIdentityNumber/{id}")
    public ResponseEntity<List<? extends ResponseContract>> getUserByIdNo(@RequestBody IdentityNoRequest request) {
        UsersService.setServiceHandler("getUsersByIdentityNo");
        service.setIdentityNoRequest(request);
        var list = this.serviceConcurrentExecutor.buildServiceExecutor(service);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/cache")
    public void cache(){
        var cache = config.cache("UsersSessionID");
    }
}
