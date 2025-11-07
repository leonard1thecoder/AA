package com.retails.application.mapper;


import com.retails.application.dto.RegisterRetailCompanyRequest;
import com.retails.application.dto.RetailCompanyResponse;
import com.retails.application.entity.RetailCompany;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface RetailCompanyMapper {
    RetailCompanyResponse toDto(RetailCompany request);

    RetailCompany toEntity(RegisterRetailCompanyRequest registerRetailCompanyRequest);
}
