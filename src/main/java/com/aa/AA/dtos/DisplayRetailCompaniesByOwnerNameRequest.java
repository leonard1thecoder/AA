package com.aa.AA.dtos;

import com.aa.AA.entities.UsersEntity;

public class DisplayRetailCompaniesByOwnerNameRequest {

    private UsersEntity usersEntity;

    public UsersEntity getUsersEntity() {
        return usersEntity;
    }

    public void setUsersEntity(UsersEntity usersEntity) {
        this.usersEntity = usersEntity;
    }
}
