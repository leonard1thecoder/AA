package com.users.application.dtos;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;



@ToString
public class UsersResponse implements ResponseContract {
    private Long id;
    private String usersIdentityNo;
    private Short usersStatus,usersAge;
    private String usersFullName, usersEmailAddress, usersCountryName, usersRegistrationDate, usersLanguage, usersModifiedDate;
    private String token;

    @JsonCreator
    public UsersResponse(){}
    public UsersResponse(String usersIdentityNo, Short usersAge, Short usersStatus, String usersFullName, String usersEmailAddress, String usersRegistrationDate, String usersCountryName, String usersLanguage, String usersModifiedDate) {
        this.usersIdentityNo = usersIdentityNo;
        this.usersAge = usersAge;
        this.usersStatus = usersStatus;
        this.usersFullName = usersFullName;
        this.usersEmailAddress = usersEmailAddress;
        this.usersRegistrationDate = usersRegistrationDate;
        this.usersCountryName = usersCountryName;
        this.usersLanguage = usersLanguage;
        this.usersModifiedDate = usersModifiedDate;
    }

    public String getUsersIdentityNo() {
        return usersIdentityNo;
    }

    public void setUsersIdentityNo(String usersIdentityNo) {
        this.usersIdentityNo = usersIdentityNo;
    }

    public String getUsersFullName() {
        return usersFullName;
    }

    public void setUsersFullName(String usersFullName) {
        this.usersFullName = usersFullName;
    }

    public Short getUsersAge() {
        return usersAge;
    }

    public void setUsersAge(Short usersAge) {
        this.usersAge = usersAge;
    }

    public Short getUsersStatus() {
        return usersStatus;
    }

    public void setUsersStatus(Short usersStatus) {
        this.usersStatus = usersStatus;
    }

    public String getUsersEmailAddress() {
        return usersEmailAddress;
    }

    public void setUsersEmailAddress(String usersEmailAddress) {
        this.usersEmailAddress = usersEmailAddress;
    }

    public String getUsersCountryName() {
        return usersCountryName;
    }

    public void setUsersCountryName(String usersCountryName) {
        this.usersCountryName = usersCountryName;
    }

    public String getUsersRegistrationDate() {
        return usersRegistrationDate;
    }

    public void setUsersRegistrationDate(String usersRegistrationDate) {
        this.usersRegistrationDate = usersRegistrationDate;
    }

    public String getUsersLanguage() {
        return usersLanguage;
    }

    public void setUsersLanguage(String usersLanguage) {
        this.usersLanguage = usersLanguage;
    }

    public String getUsersModifiedDate() {
        return usersModifiedDate;
    }

    public void setUsersModifiedDate(String usersModifiedDate) {
        this.usersModifiedDate = usersModifiedDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
