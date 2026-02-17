package com.retails.application.dto;

import com.privileges.application.entity.Privileges;
import com.users.application.entities.Users;
import com.utils.application.RequestContract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class RegisterRetailCompanyRequest implements RequestContract {

    private Users user;
    private Privileges privilege;
    private String retailCompanyName,countryName,cityName,retailCompanyCertNo;

    private Byte retailCompanyStatus;

    }
