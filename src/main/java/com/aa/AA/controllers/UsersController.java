package com.aa.AA.controllers;

import com.aa.AA.dtos.UsersRequest;
import com.aa.AA.services.UsersService;
import com.aa.AA.utils.executors.UsersServiceConcurrentExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dev/api")
public class UsersController {

    private UsersService service;
    private UsersServiceConcurrentExecutor usersServiceConcurrentExecutor;

    @Autowired
    public UsersController(@Autowired UsersService service,@Autowired UsersServiceConcurrentExecutor usersServiceConcurrentExecutor) {
        this.usersServiceConcurrentExecutor = usersServiceConcurrentExecutor;
        this.service = service;
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UsersRequest>> getAllUsers() {
        UsersService.setServiceHandler("getAllUsers");
        var list = this.usersServiceConcurrentExecutor.buildServiceExecutor();

        if (list.isEmpty())
          return ResponseEntity.notFound().build();
        else if (list == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok(list);
    }


    @GetMapping("/getAllUsers/{id}")
    public ResponseEntity<List<UsersRequest>> getUserById(@PathVariable Long id) {
        UsersService.setServiceHandler("getUsersById");
        service.setPkUsersId(id);
        var list = this.usersServiceConcurrentExecutor.buildServiceExecutor(service);

        if (list.isEmpty())
            return ResponseEntity.notFound().build();
        else if (list == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok(list);
    }
}
