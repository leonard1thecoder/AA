package com.users.application.dtos;


import com.utils.application.RequestContract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class IdentityNoRequest implements RequestContract {
    private String usersIdentityNo;
}
