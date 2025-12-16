package com.users.application.controllers;



import com.users.application.dtos.*;
import com.users.application.mappers.UsersMapper;
import com.users.application.services.UsersService;
import com.utils.application.ResponseContract;
import com.users.application.executor.ServiceConcurrentExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/dev/api/auth")
public class UsersAuthController {
    private UsersService service;
    private ServiceConcurrentExecutor serviceConcurrentExecutor;
    private UsersMapper mapper;
    @Autowired
    public UsersAuthController(@Autowired UsersMapper mapper, @Autowired UsersService service) {
        this.serviceConcurrentExecutor = ServiceConcurrentExecutor.getInstance();
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping("/register")
    public ResponseEntity<List<? extends ResponseContract>> userRegisters(@RequestBody UsersRegisterRequest request, UriComponentsBuilder uriBuilder){

        service.setUsersRegisterRequest(request);
        UsersService.setServiceHandler("registerUsers");
        var set = serviceConcurrentExecutor.buildServiceExecutor(service);

        if (set.isEmpty()) {
            return ResponseEntity.badRequest().build();
        } else {
            // creating status 201
            var uri = uriBuilder.path("/dev/api/findUserByIdentityNumber/{id}").buildAndExpand(request.getUserIdentityNo()).toUri();
            return ResponseEntity.created(uri).body(set);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<List<? extends ResponseContract>> login(@RequestBody LoginRequest request) {
        UsersService.setServiceHandler("userLogin");
        service.setLoginRequest(request);
        var list = this.serviceConcurrentExecutor.buildServiceExecutor(service);
        if (list.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(list);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<Void> updateUsersPassword( @RequestBody UpdatePasswordRequest request) {
        UsersService.setServiceHandler("userUpdatePassword");
        service.setUpdatePasswordRequest(request);


            var list = this.serviceConcurrentExecutor.buildServiceExecutor(service);

            if (list.isEmpty())
                return ResponseEntity.notFound().build();
            else return ResponseEntity.noContent().build();

    }
}
