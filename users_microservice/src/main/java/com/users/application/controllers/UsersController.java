package com.users.application.controllers;


import com.users.application.dtos.*;

import com.users.application.services.UsersService;
import com.utils.application.ResponseContract;
import com.users.application.executor.ServiceConcurrentExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dev/api/users/")
public class UsersController {

    private UsersService service;
    private ServiceConcurrentExecutor serviceConcurrentExecutor;


    @Autowired

    public UsersController( @Autowired UsersService service) {
        this.serviceConcurrentExecutor = ServiceConcurrentExecutor.getInstance();
        this.service = service;

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

}
