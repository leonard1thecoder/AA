package com.retails.application.dto;


import com.users.application.entities.Users;

public class DisplayRetailCompaniesByOwnerNameRequest {

    private Users usersEntity;

    public Users getUsersEntity() {
        return usersEntity;
    }

    public void setUsersEntity(Users usersEntity) {
        this.usersEntity = usersEntity;
    }
}
