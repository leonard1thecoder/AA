package com.aa.AA.dtos;

import com.aa.AA.entities.PrivilegeEntity;

public class UsersRegisterRequest {
 
    private PrivilegeEntity fkPrivilegeId;
    
    private Long usersIdentityNo;

    private Integer noPromotionToken;
  
    private Short usersStatus, usersAge;
  
    private String usersFullName, usersEmailAddress, usersPassword, usersRegistrationDate, usersModifiedDate;


    public UsersRegisterRequest(PrivilegeEntity fkPrivilegeId, String usersRegistrationDate, String usersPassword, String usersEmailAddress, String usersFullName, Short usersAge, Short usersStatus, Integer noPromotionToken, Long usersIdentityNo, String usersModifiedDate) {
        this.fkPrivilegeId = fkPrivilegeId;
        this.usersRegistrationDate = usersRegistrationDate;
        this.usersPassword = usersPassword;
        this.usersEmailAddress = usersEmailAddress;
        this.usersFullName = usersFullName;
        this.usersAge = usersAge;
        this.usersStatus = usersStatus;
        this.noPromotionToken = noPromotionToken;
        this.usersIdentityNo = usersIdentityNo;
        this.usersModifiedDate = usersModifiedDate;
    }

    public PrivilegeEntity fkPrivilegeId() {
        return fkPrivilegeId;
    }

    public UsersRegisterRequest setFkPrivilegeId(PrivilegeEntity fkPrivilegeId) {
        this.fkPrivilegeId = fkPrivilegeId;
        return this;
    }

    public Long usersIdentityNo() {
        return usersIdentityNo;
    }

    public UsersRegisterRequest setUsersIdentityNo(Long usersIdentityNo) {
        this.usersIdentityNo = usersIdentityNo;
        return this;
    }

    public Integer noPromotionToken() {
        return noPromotionToken;
    }

    public UsersRegisterRequest setNoPromotionToken(Integer noPromotionToken) {
        this.noPromotionToken = noPromotionToken;
        return this;
    }

    public Short usersStatus() {
        return usersStatus;
    }

    public UsersRegisterRequest setUsersStatus(Short usersStatus) {
        this.usersStatus = usersStatus;
        return this;
    }

    public Short usersAge() {
        return usersAge;
    }

    public UsersRegisterRequest setUsersAge(Short usersAge) {
        this.usersAge = usersAge;
        return this;
    }

    public String usersEmailAddress() {
        return usersEmailAddress;
    }

    public UsersRegisterRequest setUsersEmailAddress(String usersEmailAddress) {
        this.usersEmailAddress = usersEmailAddress;
        return this;
    }

    public String usersFullName() {
        return usersFullName;
    }

    public UsersRegisterRequest setUsersFullName(String usersFullName) {
        this.usersFullName = usersFullName;
        return this;
    }

    public String usersPassword() {
        return usersPassword;
    }

    public UsersRegisterRequest setUsersPassword(String usersPassword) {
        this.usersPassword = usersPassword;
        return this;
    }

    public String usersRegistrationDate() {
        return usersRegistrationDate;
    }

    public UsersRegisterRequest setUsersRegistrationDate(String usersRegistrationDate) {
        this.usersRegistrationDate = usersRegistrationDate;
        return this;
    }

    public String usersModifiedDate() {
        return usersModifiedDate;
    }

    public UsersRegisterRequest setUsersModifiedDate(String usersModifiedDate) {
        this.usersModifiedDate = usersModifiedDate;
        return this;
    }
}
