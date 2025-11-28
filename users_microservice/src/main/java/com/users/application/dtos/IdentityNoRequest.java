package com.users.application.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class IdentityNoRequest {
    private String usersIdentityNo;
}
