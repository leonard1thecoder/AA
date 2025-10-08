package com.aa.AA.dtos;

public class UsersFullNameRequest {
    private String usersFullName;

    public UsersFullNameRequest(String usersFullName) {
        this.usersFullName = usersFullName;
    }

    public String usersFullName() {
        return usersFullName;
    }

    public UsersFullNameRequest setUsersFullName(String usersFullName) {
        this.usersFullName = usersFullName;
        return this;
    }
}
