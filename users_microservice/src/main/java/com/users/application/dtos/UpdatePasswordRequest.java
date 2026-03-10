package com.users.application.dtos;

import com.utils.application.RequestContract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class UpdatePasswordRequest implements RequestContract {


    private String usersPassword,userToken;
    private String usersConfirmPassword;

}
