package com.aa.AA.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UsersRequest {
    private Long usersIdentityNo;
    private Integer ftPrivilegeId;
    private Short usersStatus,usersAge;
    private String usersFullName, usersEmailAddress, usersCountryName, usersRegistrationDate, usersLanguage, usersModifiedDate;

}
