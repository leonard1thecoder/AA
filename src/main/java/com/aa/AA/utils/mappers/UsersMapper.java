package com.aa.AA.utils.mappers;


import com.aa.AA.dtos.UsersRegisterRequest;
import com.aa.AA.dtos.UsersRequest;
import com.aa.AA.entities.UsersEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component

public interface UsersMapper {
    UsersRequest toDto(UsersEntity request);
    UsersEntity toEntity(UsersRegisterRequest request);
}
