package com.retails.application.dto;


import com.privileges.application.entity.Privileges;
import com.users.application.entities.Users;
import com.utils.application.ResponseContract;

public class RetailCompanyResponse implements ResponseContract {

    private Long id;


    private Users usersEntity;


    private Privileges privilegeEntity;

    private String liquorStoreName,countryName,cityName,liquorStoreCertNo;

    private Byte liquorStoreStatus;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
