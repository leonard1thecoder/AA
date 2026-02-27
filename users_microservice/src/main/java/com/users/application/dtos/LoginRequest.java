package com.users.application.dtos;


import com.utils.application.RequestContract;
import lombok.*;


@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class LoginRequest implements RequestContract {
    private String usersEmailAddress;
    private String usersPassword;

}
