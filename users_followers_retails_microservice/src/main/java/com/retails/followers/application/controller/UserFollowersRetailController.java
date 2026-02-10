package com.retails.followers.application.controller;

import com.retails.followers.application.dto.GetFollowedCompaniesRequest;
import com.retails.followers.application.dto.UserFollowersRetailRequest;
import com.retails.followers.application.dto.UserFollowersRetailResponse;
import com.retails.followers.application.executor.UserFollowsRetailServiceExecutor;
import com.retails.followers.application.service.UserFollowersRetailService;
import com.utils.application.ResponseContract;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("dev/api/follow")
public class UserFollowersRetailController {

    private final UserFollowersRetailService service;
    private final UserFollowsRetailServiceExecutor executor;
    public UserFollowersRetailController(UserFollowersRetailService service) {
        this.executor = UserFollowsRetailServiceExecutor.getInstance();
        this.service = service;
    }

    @PostMapping("/company/{companyId}/user/{userId}")
    public ResponseEntity<List<? extends ResponseContract>> follow(@PathVariable Long userId, @PathVariable Long companyId) {
        var response = executor.buildContactFormServiceExecutor(service, UserFollowersRetailRequest
                .builder()
                        .fk_user_id(userId)
                        .fk_retail_company_id(companyId)
                .build(),
                "follow"
                );

        if (response.getFirst() instanceof UserFollowersRetailResponse){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/company/{companyId}/user/{userId}/unfollow")
    public ResponseEntity<List<? extends ResponseContract>>  unfollow(@PathVariable Long userId, @PathVariable Long companyId) {
        var response = executor.buildContactFormServiceExecutor(service, UserFollowersRetailRequest
                .builder()
                .fk_user_id(userId)
                .fk_retail_company_id(companyId)
                .build(),"unfollow");

        if (response.getFirst() instanceof UserFollowersRetailResponse){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<? extends ResponseContract>>  getUserFollows(@PathVariable Long userId) {
        var response = executor.buildContactFormServiceExecutor(service, GetFollowedCompaniesRequest
                .builder()
                .fk_user_id(userId)
                .build()
                ,"getFollowedCompanies"
                );

        if (response.getFirst() instanceof UserFollowersRetailResponse){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<? extends ResponseContract>>  getCompanyFollowers(@PathVariable Long companyId) {
        var response = executor.buildContactFormServiceExecutor(service, GetFollowedCompaniesRequest
                .builder()
                .fk_user_id(companyId)
                .build()
                ,"getCompanyFollowers");

        if (response.getFirst() instanceof UserFollowersRetailResponse){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.badRequest().body(response);
        }    }
}
