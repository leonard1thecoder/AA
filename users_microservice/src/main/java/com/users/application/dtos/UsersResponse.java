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
public class UsersResponse implements ResponseContract {
    private Long id;
    private Privileges privileges;
    private String usersIdentityNo;
    private Short usersStatus,usersAge;
    private String usersFullName,cellphoneNo, usersEmailAddress, usersRegistrationDate, usersModifiedDate;


    @JsonCreator
    public UsersResponse(){}
    public UsersResponse(String usersIdentityNo, Short usersAge, Short usersStatus, String usersFullName, String usersEmailAddress, String usersRegistrationDate, String usersCountryName, String usersLanguage, String usersModifiedDate) {
        this.usersIdentityNo = usersIdentityNo;
        this.usersAge = usersAge;
        this.usersStatus = usersStatus;
        this.usersFullName = usersFullName;
        this.usersEmailAddress = usersEmailAddress;
        this.usersRegistrationDate = usersRegistrationDate;

        this.usersModifiedDate = usersModifiedDate;
    }



    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
      this.id = id;
    }
}
