package com.users.application.dtos;


import com.privileges.application.entity.Privileges;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Builder
@AllArgsConstructor
public class UsersRegisterRequest {

    private String userCellphoneNo;
    private Integer privileges;

    private String userIdentityNo;


    private Short userStatus;

    private String userFullName, userEmailAddress, userPassword;



}
