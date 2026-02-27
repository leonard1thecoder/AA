package com.users.application.dtos;


import com.utils.application.RequestContract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RollBackPasswordRequest implements RequestContract {

    private String  token,emailAddress;
}
