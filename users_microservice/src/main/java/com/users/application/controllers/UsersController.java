package com.users.application.controllers;


import com.users.application.dtos.*;

import com.users.application.services.UsersService;
import com.utils.application.ResponseContract;
import com.users.application.executor.ServiceConcurrentExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.utils.application.globalExceptions.errorResponse.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/dev/api/users")
public class UsersController {
    Logger logger = LoggerFactory.getLogger(UsersController.class);

    private final UsersService service;
    private final ServiceConcurrentExecutor serviceConcurrentExecutor;


    @Autowired

    public UsersController( @Autowired UsersService service) {
        this.serviceConcurrentExecutor = ServiceConcurrentExecutor.getInstance();
        this.service = service;

    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<? extends ResponseContract>> getAllUsers() {
        UsersService.setServiceHandler("getAllUsers");
        var list = this.serviceConcurrentExecutor.buildServiceExecutor(service);
        return ResponseEntity.ok(list);
    }


    @GetMapping("/getAllUsers/{id}")
    public ResponseEntity<List<? extends ResponseContract>> getUserById(@PathVariable Long id) {
        UsersService.setServiceHandler("getUsersById");
        service.setFindByIdRequest(new FindByIdRequest(id));
        var list = this.serviceConcurrentExecutor.buildServiceExecutor(service).get(0);
 if (list instanceof UsersResponse users) {
            return ResponseEntity.ok(List.of(users));
        } else {

            ErrorResponse error = (ErrorResponse) list;
            logger.warn("Error response : {}", error);
           return ResponseEntity.badRequest().body(List.of(error));
        }  
    }

    @GetMapping("/getUsersByFullName/{fullName}")
    public ResponseEntity<List<? extends ResponseContract>> getUserByFullName(@PathVariable String fullName) {
        UsersService.setServiceHandler("getUsersByFullName");
        service.setUsersFullNameRequest(new UsersFullNameRequest(fullName));
        var list = this.serviceConcurrentExecutor.buildServiceExecutor(service).get(0);
 if (list instanceof UsersResponse users) {
            return ResponseEntity.ok(List.of(users));
        } else {

            ErrorResponse error = (ErrorResponse) list;
            logger.warn("Error response : {}", error);
           return ResponseEntity.badRequest().body(List.of(error));
        }    }

    @PostMapping("/findUserByIdentityNumber/{id}")
    public ResponseEntity<List<? extends ResponseContract>> getUserByIdNo(@RequestBody IdentityNoRequest request) {
        UsersService.setServiceHandler("getUsersByIdentityNo");
        service.setIdentityNoRequest(request);
        var list = this.serviceConcurrentExecutor.buildServiceExecutor(service);
        return ResponseEntity.ok(list);
    }

}
