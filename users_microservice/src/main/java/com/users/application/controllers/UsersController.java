package com.users.application.controllers;


import com.users.application.dtos.*;

import com.users.application.executor.UserServiceConcurrentExecutor;
import com.users.application.services.UsersService;
import com.utils.application.ResponseContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.utils.application.globalExceptions.errorResponse.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/dev/api/users")
public class UsersController extends UserServiceConcurrentExecutor {
    Logger logger = LoggerFactory.getLogger(UsersController.class);

    private final UsersService service;
    


    @Autowired
    public UsersController(@Autowired UsersService service) {
        super(Executors.newVirtualThreadPerTaskExecutor());
        this.service = service;
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<? extends ResponseContract>> getAllUsers() {

        var list = super.executeUserService(service, "getAllUsers", null);
        return ResponseEntity.ok(list);
    }


    @GetMapping("/getAllUsers/{id}")
    public ResponseEntity<List<? extends ResponseContract>> getUserById(@PathVariable Long id) {

        var list = super.executeUserService(service, "getUsersById", new FindByIdRequest(id)).getFirst();
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
        var list = super.executeUserService(service,"getUsersByFullName",new UsersFullNameRequest(fullName)).getFirst();
        if (list instanceof UsersResponse users) {
            return ResponseEntity.ok(List.of(users));
        } else {

            ErrorResponse error = (ErrorResponse) list;
            logger.warn("Error response : {}", error);
            return ResponseEntity.badRequest().body(List.of(error));
        }
    }

    @PostMapping("/findUserByIdentityNumber/{id}")
    public ResponseEntity<List<? extends ResponseContract>> getUserByIdNo(@RequestBody IdentityNoRequest request) {
        var list = super.executeUserService(service,"getUsersByIdentityNo",request).getFirst();

        if (list instanceof UsersResponse) {
            return ResponseEntity.ok(List.of(list));
        }else{
            ErrorResponse error = (ErrorResponse) list;
            logger.warn("Error response : {}", error);
            return ResponseEntity.badRequest().body(List.of(error));
        }
    }

}
