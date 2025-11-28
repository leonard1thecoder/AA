package com.users.application.dtos;


import lombok.*;


@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class LoginRequest {
    private String usersEmailAddress;
    private String usersPassword;

}
