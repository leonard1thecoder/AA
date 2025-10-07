package com.aa.AA.dtos;

import lombok.Setter;

public class UpdatePasswordRequest {

    @Setter
    private String usersPassword;
    @Setter
    private String usersConfirmPassword;

    public UpdatePasswordRequest(String usersPassword, String usersConfirmPassword) {

        this.usersPassword = usersPassword;
        this.usersConfirmPassword = usersConfirmPassword;
    }


    public String getUsersConfirmPassword() {
        return usersConfirmPassword;
    }

    public String getUsersPassword() {
        return usersPassword;
    }



    public void setUsersPassword(String usersPassword) {
        this.usersPassword = usersPassword;
    }

    public void setUsersConfirmPassword(String usersConfirmPassword) {
        this.usersConfirmPassword = usersConfirmPassword;
    }
}
