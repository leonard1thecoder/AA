package com.users.application.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class UsersRegisterRequest {

    private String userCellphoneNo;
    private Integer privileges;

    private String userIdentityNo;


    private Short userStatus;

    private String userFullName, userEmailAddress, userPassword,confirmPassword;



}
