package com.aa.AA.utils.mappers;


import com.aa.AA.dtos.*;
import com.aa.AA.entities.UsersEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component

public interface UsersMapper {
    UsersRequest toDto(UsersEntity request);
    UsersEntity toEntity(UsersRegisterRequest request);
    UsersEntity toEntity(UpdatePasswordRequest request);
    UsersEntity toEntity(LoginRequest request);
    UsersEntity toEntity(IdentityNoRequest request);
    UsersEntity toEntity(UsersFullNameRequest request);

}
