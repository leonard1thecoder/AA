package com.users.application.controllers;



import com.users.application.dtos.*;
import com.users.application.mappers.UsersMapper;
import com.users.application.services.UsersService;
import com.utils.application.ResponseContract;
import com.users.application.executor.ServiceConcurrentExecutor;
import com.utils.application.globalExceptions.errorResponse.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@RestController
@RequestMapping("/dev/api/auth")
public class UsersAuthController {
    Logger logger = LoggerFactory.getLogger(UsersAuthController.class);
    private UsersService service;
    private ServiceConcurrentExecutor serviceConcurrentExecutor;
    private UsersMapper mapper;
    @Autowired
    public UsersAuthController(@Autowired UsersMapper mapper, @Autowired UsersService service) {
        this.serviceConcurrentExecutor = ServiceConcurrentExecutor.getInstance();
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<List<? extends ResponseContract>> userRegisters(@RequestBody UsersRegisterRequest request, UriComponentsBuilder uriBuilder){

        service.setUsersRegisterRequest(request);
        UsersService.setServiceHandler("registerUsers");
        var set = serviceConcurrentExecutor.buildServiceExecutor(service);

        if (set.get(0) instanceof UsersResponse) {

            // creating status 201
            var uri = uriBuilder.path("/dev/api/findUserByIdentityNumber/{id}").buildAndExpand(request.getUserIdentityNo()).toUri();
            return ResponseEntity.created(uri).body(set);
        } else {

            ErrorResponse error = (ErrorResponse) set;
            logger.warn("Error response : {}", error);
            return ResponseEntity.badRequest().body(List.of(error));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<List<? extends ResponseContract>> login(@RequestBody LoginRequest request) {
        UsersService.setServiceHandler("userLogin");
        service.setLoginRequest(request);
        var list = this.serviceConcurrentExecutor.buildServiceExecutor(service).get(0);
        if (list instanceof UsersResponse users) {
            return ResponseEntity.ok(List.of(users));
        } else {

            ErrorResponse error = (ErrorResponse) list;
            logger.warn("Error response : {}", error);
           return ResponseEntity.badRequest().body(List.of(error));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<List<? extends ResponseContract>> resetPassword( @RequestBody UpdatePasswordRequest request) {
        UsersService.setServiceHandler("reset-password");
        service.setUpdatePasswordRequest(request);


            var list = this.serviceConcurrentExecutor.buildServiceExecutor(service);

        if (list.get(0) instanceof UsersResponse) {

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
        UsersService.setFindByEmailRequest(request);
        UsersService.setServiceHandler("forgot-password");
        var response = serviceConcurrentExecutor.buildServiceExecutor(service);
        if (response.get(0) instanceof UsersResponse) {

            return ResponseEntity.ok(response);
        } else {

            ErrorResponse error = (ErrorResponse) response;
            logger.warn("Error response : {}", error);
            return ResponseEntity.badRequest().body(List.of(error));
        }
    }
}
