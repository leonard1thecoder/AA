package com.aa.AA.controllers;

import com.aa.AA.dtos.LoginRequest;
import com.aa.AA.dtos.UpdatePasswordRequest;
import com.aa.AA.dtos.UsersRequest;
import com.aa.AA.services.UsersService;
import com.aa.AA.utils.executors.UsersServiceConcurrentExecutor;
import com.aa.AA.utils.mappers.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dev/api")
public class UsersController {

    private UsersService service;
    private UsersServiceConcurrentExecutor usersServiceConcurrentExecutor;
    private UsersMapper mapper;
    @Autowired
    public UsersController(@Autowired UsersMapper mapper,@Autowired UsersService service,@Autowired UsersServiceConcurrentExecutor usersServiceConcurrentExecutor) {
        this.usersServiceConcurrentExecutor = usersServiceConcurrentExecutor;
        this.service = service;
        this.mapper = mapper;
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

    @GetMapping("/getUsersByFullName/{UserFullName}")
    public ResponseEntity<List<UsersRequest>> getUserByFullName(@PathVariable String UserFullName) {
        UsersService.setServiceHandler("getUsersByFullName");
        service.setUsersFullName(UserFullName);
        var list = this.usersServiceConcurrentExecutor.buildServiceExecutor(service);

        if (list.isEmpty())
            return ResponseEntity.notFound().build();
        else if (list == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok(list);
    }

    @PutMapping("/updatePassword/{usersEmailAddress}")
    public ResponseEntity<Void> updateUsersPassword(@PathVariable String usersEmailAddress, @RequestBody UpdatePasswordRequest request) {
        UsersService.setServiceHandler("getUsersByFullName");
        service.setUsersEmailAddress(usersEmailAddress);

        if(request.getUsersPassword().equals(request.getUsersConfirmPassword())) {
            var list = this.usersServiceConcurrentExecutor.buildServiceExecutor(service);

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
    @PostMapping("/login")
    public ResponseEntity<List<UsersRequest>> login(@RequestBody LoginRequest request) {
        UsersService.setServiceHandler("getUsersByFullName");
        service.setUsersPassword(request.getUsersPassword());
        service.setUsersEmailAddress(request.getUsersEmailAddress());
        var list = this.usersServiceConcurrentExecutor.buildServiceExecutor(service);

        if (list.isEmpty())
            return ResponseEntity.notFound().build();
        else if (list == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok(list);
    }

    @GetMapping("/findUserByIdentityNumber/{id}")
    public ResponseEntity<List<UsersRequest>> getUserByIdNo(@PathVariable Long id) {
        UsersService.setServiceHandler("getUsersByIdentityNo");
        service.setUsersIdentityNo(id);
        var list = this.usersServiceConcurrentExecutor.buildServiceExecutor(service);

        if (list.isEmpty())
            return ResponseEntity.notFound().build();
        else if (list == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok(list);
    }
}
