package com.users.application.controllers;

import com.users.application.dtos.*;
import com.users.application.executor.UserServiceConcurrentExecutor;
import com.users.application.services.UsersService;
import com.utils.application.ResponseContract;
import com.utils.application.globalExceptions.errorResponse.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;
import java.util.concurrent.Executors;


@RestController
@RequestMapping("/dev/api/auth")
public class UsersAuthController extends UserServiceConcurrentExecutor {
   private final Logger logger = LoggerFactory.getLogger(UsersAuthController.class);
    private final UsersService service;
    @Autowired
    public UsersAuthController( @Autowired UsersService service) {
       super(Executors.newVirtualThreadPerTaskExecutor());
        this.service = service;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<List<? extends ResponseContract>> userRegisters(@RequestBody UsersRegisterRequest request, UriComponentsBuilder uriBuilder){

        var set = super.executeUserService(service,"registerUsers",request);

        if (set.getFirst() instanceof UsersResponse) {

            // creating status 201
            var uri = uriBuilder.path("/dev/api/findUserByIdentityNumber/{id}").buildAndExpand(request.getUserIdentityNo()).toUri();
            return ResponseEntity.created(uri).body(set);
        } else {
            ErrorResponse error = (ErrorResponse) set.getFirst();
            logger.warn("Error response : {}", error);
            return ResponseEntity.badRequest().body(List.of(error));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<List<? extends ResponseContract>> login(@RequestBody LoginRequest request) {
        var list = super.executeUserService(service,"userLogin",request).getFirst();
        if (list instanceof UsersResponse users) {
            return ResponseEntity.ok(List.of(users));
        } else {
            ErrorResponse error = (ErrorResponse) list;
            logger.warn("Error response : {}, error stack : {}", error, error.getException().getStackTrace());
           return ResponseEntity.badRequest().body(List.of(error));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<List<? extends ResponseContract>> resetPassword( @RequestBody UpdatePasswordRequest request) {
            var list = super.executeUserService(service,"reset-password",request);

        if (list.getFirst() instanceof UsersResponse) {
            list.clear();
            return ResponseEntity.ok(list);
        } else {

            ErrorResponse error = (ErrorResponse) list;
            logger.warn("Error response : {}", error);
            return ResponseEntity.badRequest().body(List.of(error));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<List<? extends ResponseContract>> forgotPassword(@RequestBody FindByEmailRequest request){

        var response = super.executeUserService(service,"forgot-password",request);
        if (response.getFirst() instanceof UsersResponse) {
            return ResponseEntity.ok(response);
        } else {
            ErrorResponse error = (ErrorResponse) response.getFirst();
            logger.warn("Error response : {}", error);
            return ResponseEntity.badRequest().body(List.of(error));
        }
    }
}
