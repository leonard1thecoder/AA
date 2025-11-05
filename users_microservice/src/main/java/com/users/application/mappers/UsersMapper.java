package com.users.application.mappers;




import com.users.application.dtos.*;
import com.users.application.entities.Users;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component

public interface UsersMapper {
    UsersResponse toDto(Users request);
    Users toEntity(UsersRegisterRequest request);
    Users toEntity(UpdatePasswordRequest request);
    Users toEntity(LoginRequest request);
    Users toEntity(IdentityNoRequest request);
    Users toEntity(UsersFullNameRequest request);

}
