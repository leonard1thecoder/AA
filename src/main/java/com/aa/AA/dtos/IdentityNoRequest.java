package com.aa.AA.dtos;

public class IdentityNoRequest {
    private Long usersIdentityNo;

    public IdentityNoRequest(Long usersIdentityNo) {
        this.usersIdentityNo = usersIdentityNo;
    }

    public Long usersIdentityNo() {
        return usersIdentityNo;
    }

    public IdentityNoRequest setUsersIdentityNo(Long usersIdentityNo) {
        this.usersIdentityNo = usersIdentityNo;
        return this;
    }
}
