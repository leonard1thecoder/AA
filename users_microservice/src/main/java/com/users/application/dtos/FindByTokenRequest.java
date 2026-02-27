package com.users.application.dtos;


import com.utils.application.RequestContract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FindByTokenRequest implements RequestContract {
    private String token;
}

