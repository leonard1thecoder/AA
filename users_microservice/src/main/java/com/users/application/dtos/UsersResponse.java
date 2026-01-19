package com.users.application.dtos;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.privileges.application.entity.Privileges;
import com.utils.application.ResponseContract;
import lombok.*;
import org.springframework.stereotype.Component;


@Data
@Builder
@AllArgsConstructor
@Component
public class UsersResponse implements ResponseContract,java.io.Serializable {
    private Long id;
    private Integer privileges;
    private String usersIdentityNo;
    private Short usersStatus,usersAge,updatePasswordStatus;
    private String usersFullName,cellphoneNo, usersEmailAddress, usersRegistrationDate, usersModifiedDate;
    private String  token,status;

    @JsonCreator
    public UsersResponse(){}

}
