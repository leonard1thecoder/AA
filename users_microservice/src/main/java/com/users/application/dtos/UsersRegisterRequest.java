package com.users.application.dtos;


import com.privileges.application.entity.Privileges;
import com.utils.application.RequestContract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class UsersRegisterRequest implements RequestContract {

    private String userCellphoneNo;
    private Privileges privileges;

    private String userIdentityNo;


    private Short userStatus;

    private String userFullName, userEmailAddress, userPassword,confirmPassword;



}
