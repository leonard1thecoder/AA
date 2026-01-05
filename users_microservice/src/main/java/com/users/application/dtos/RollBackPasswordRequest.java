package com.users.application.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RollBackPasswordRequest {

    private String  token,emailAddress;
}
