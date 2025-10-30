package com.aa.AA.controllers;


import com.aa.AA.dtos.LoginRequest;
import com.aa.AA.dtos.UpdatePasswordRequest;
import com.aa.AA.dtos.UsersRegisterRequest;
import com.aa.AA.dtos.UsersResponse;
import com.aa.AA.services.UsersService;
import com.aa.AA.utils.executors.ResponseContract;
import com.aa.AA.utils.executors.ServiceConcurrentExecutor;
import com.aa.AA.utils.mappers.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/dev/api/auth/")
public class UsersAuthController {
    private UsersService service;
    private ServiceConcurrentExecutor serviceConcurrentExecutor;
    private UsersMapper mapper;
    @Autowired
    public UsersAuthController(@Autowired UsersMapper mapper, @Autowired UsersService service, @Autowired ServiceConcurrentExecutor serviceConcurrentExecutor) {
        this.serviceConcurrentExecutor = serviceConcurrentExecutor;
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
            var uri = uriBuilder.path("/dev/api/findUserByIdentityNumber/{id}").buildAndExpand(request.getUsersIdentityNo()).toUri();
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
        else if (list == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok(list);
    }

    @PutMapping("/{usersEmailAddress}")
    public ResponseEntity<Void> updateUsersPassword(@PathVariable String usersEmailAddress, @RequestBody UpdatePasswordRequest request) {
        UsersService.setServiceHandler("getUsersByFullName");
        service.setUpdatePasswordRequest(request);

        if(request.getUsersPassword().equals(request.getUsersConfirmPassword())) {
            var list = this.serviceConcurrentExecutor.buildServiceExecutor(service);

            if (list.isEmpty())
                return ResponseEntity.notFound().build();
            else if (list == null)
                return ResponseEntity.badRequest().build();
            else
                return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.badRequest().build();
        }
    }
}
