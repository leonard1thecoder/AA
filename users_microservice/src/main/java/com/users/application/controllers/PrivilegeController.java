package com.users.application.controllers;

import com.privileges.application.dtos.AddPrivilegesRequest;
import com.privileges.application.dtos.FindByNameRequest;
import com.privileges.application.dtos.PrivilegesResponse;
import com.privileges.application.service.PrivilegesService;
import com.users.application.executor.ServiceConcurrentExecutor;
import com.utils.application.ResponseContract;
import com.utils.application.globalExceptions.errorResponse.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/dev/privileges/api")
public class PrivilegeController {
    Logger logger = LoggerFactory.getLogger(PrivilegeController.class);
    private final PrivilegesService privilegesService;
    private final ServiceConcurrentExecutor serviceConcurrentExecutor;


    public PrivilegeController(PrivilegesService privilegesService) {
        this.privilegesService = privilegesService;
        this.serviceConcurrentExecutor = ServiceConcurrentExecutor.getInstance();
    }

    @PostMapping("/addPrivilege")
    public ResponseEntity<List<? extends ResponseContract>> addPrivileges(@RequestBody AddPrivilegesRequest request, UriComponentsBuilder uriBuilder) {
        privilegesService.setAddPrivilegesRequest(request);

        var list = this.serviceConcurrentExecutor.buildServiceExecutor(privilegesService, "AddPrivileges").get(0);
        if (list instanceof PrivilegesResponse response) {

            // creating status 201
            var uri = uriBuilder.path("/dev/privileges/api/findByName/{name}").buildAndExpand(request.getPrivilegeName()).toUri();
            return ResponseEntity.created(uri).body(List.of(response));
        } else {
            ErrorResponse error = (ErrorResponse) list;
            logger.warn("Error response : {}", error);
            return ResponseEntity.badRequest().body(List.of(error));
        }
    }

    @GetMapping("/printPrivilege")
    public ResponseEntity<List<? extends ResponseContract>> printPrivileges() {
        var list = this.serviceConcurrentExecutor.buildServiceExecutor(privilegesService, "getAllPrivileges");
        return ResponseEntity.ok(list);
    }

    @GetMapping("/findByName/{name}")
    public ResponseEntity<List<? extends ResponseContract>> printPrivilegeByName(@PathVariable String name) {
        privilegesService.setFindByPrivilegeNameRequest(FindByNameRequest
                .builder()
                .privilegeName(name)
                .build());
        var list = this.serviceConcurrentExecutor.buildServiceExecutor(privilegesService, "getPrivilegeByName").get(0);
        if (list instanceof PrivilegesResponse response) {
            return ResponseEntity.ok(List.of(response));
        } else {
            ErrorResponse error = (ErrorResponse) list;
            logger.warn("Error response : {}", error);
            return ResponseEntity.badRequest().body(List.of(error));
        }
    }
}