package com.retails.application.dto;


import com.utils.application.ResponseContract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RetailCompanyResponse implements ResponseContract {

    private Long id;

    private String retailCompanyName,countryName,cityName,retailCompanyCertNo,registrationDate,modifiedDate;

    private Byte retailCompanyStatus;

}
