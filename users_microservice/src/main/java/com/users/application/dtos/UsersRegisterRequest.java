package com.users.application.dtos;


import com.privileges.application.entity.Privileges;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UsersRegisterRequest {

    private String usersCellphoneNo;
    private Privileges fkPrivilegeId;
    
    private String usersIdentityNo;

    private Integer noPromotionToken;
  
    private Short usersStatus, usersAge;
  
    private String usersFullName, usersEmailAddress, usersPassword, usersRegistrationDate, usersModifiedDate;



}
