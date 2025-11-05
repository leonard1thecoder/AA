package com.users.application.dtos;

import lombok.Setter;

public class UpdatePasswordRequest {


    @Setter
    private String usersPassword,usersEmailAddress;
    @Setter
    private String usersConfirmPassword;


    public String getUsersEmailAddress() {
        return usersEmailAddress;
    }

    public void setUsersEmailAddress(String usersEmailAddress) {
        this.usersEmailAddress = usersEmailAddress;
    }

    public String getUsersConfirmPassword() {
        return usersConfirmPassword;
    }

    public String getUsersPassword() {
        return usersPassword;
    }

    public UpdatePasswordRequest(String usersPassword, String usersEmailAddress, String usersConfirmPassword) {
        this.usersPassword = usersPassword;
        this.usersEmailAddress = usersEmailAddress;
        this.usersConfirmPassword = usersConfirmPassword;
    }

    public void setUsersPassword(String usersPassword) {
        this.usersPassword = usersPassword;
    }

    public void setUsersConfirmPassword(String usersConfirmPassword) {
        this.usersConfirmPassword = usersConfirmPassword;
    }
}
