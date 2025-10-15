package com.aa.AA.dtos;

public class IdentityNoRequest {
    private String usersIdentityNo;

    public IdentityNoRequest(String usersIdentityNo) {
        this.usersIdentityNo = usersIdentityNo;
    }

    public String usersIdentityNo() {
        return usersIdentityNo;
    }

    public IdentityNoRequest setUsersIdentityNo(String usersIdentityNo) {
        this.usersIdentityNo = usersIdentityNo;
        return this;
    }
}
