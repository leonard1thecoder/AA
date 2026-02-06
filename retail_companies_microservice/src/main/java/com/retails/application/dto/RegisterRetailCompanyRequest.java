package com.retails.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class RegisterRetailCompanyRequest {

    private Long fk_user_id;

    private String retailCompanyName,countryName,cityName,retailCompanyCertNo;

    private Byte retailCompanyStatus,fk_privilege_id;

    }
