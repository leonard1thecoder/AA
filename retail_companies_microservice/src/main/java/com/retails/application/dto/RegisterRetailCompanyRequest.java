package com.retails.application.dto;


import com.privileges.application.entity.Privileges;
import com.users.application.entities.Users;
import lombok.Data;


@Data
public class RegisterRetailCompanyRequest {

    private Users usersEntity;

    private Privileges privilegeEntity;

    private String liquorStoreName,countryName,cityName,liquorStoreCertNo;

    private Byte liquorStoreStatus;

    }
