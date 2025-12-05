package com.users.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class UpdatePasswordRequest {


    private String usersPassword,usersEmailAddress;
    private String usersConfirmPassword;

}
