package com.aa.AA.dtos;

public class FindByIdRequest {

    private Long pkUsersId;

    public FindByIdRequest(Long pkUsersId) {
        this.pkUsersId = pkUsersId;
    }

    public Long getPkUsersId() {
        return pkUsersId;
    }

    public void setPkUsersId(Long pkUsersId) {
        this.pkUsersId = pkUsersId;
    }
}
