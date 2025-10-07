package com.aa.AA.dtos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class LoginRequest {
    private String usersEmailAddress;
    private String usersPassword;

    public LoginRequest(String usersEmailAddress, String usersPassword) {
        this.usersEmailAddress = usersEmailAddress;
        this.usersPassword = usersPassword;
    }

    public String getUsersEmailAddress() {
        return usersEmailAddress;
    }

    public String getUsersPassword() {
        return usersPassword;
    }
}
