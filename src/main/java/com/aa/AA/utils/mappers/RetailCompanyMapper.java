package com.aa.AA.utils.mappers;

import com.aa.AA.dtos.RetailCompanyResponse;
import com.aa.AA.entities.RetailCompanyEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface RetailCompanyMapper {
    RetailCompanyResponse toDto(RetailCompanyEntity request);
}
