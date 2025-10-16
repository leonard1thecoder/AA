package com.aa.AA.dtos;

import com.aa.AA.entities.PrivilegeEntity;

public class UsersRegisterRequest {
 
    private PrivilegeEntity fkPrivilegeId;
    
    private String usersIdentityNo;

    private Integer noPromotionToken;
  
    private Short usersStatus, usersAge;
  
    private String usersFullName, usersEmailAddress, usersPassword, usersRegistrationDate, usersModifiedDate;


    public UsersRegisterRequest(PrivilegeEntity fkPrivilegeId, String usersRegistrationDate, String usersPassword, String usersEmailAddress, String usersFullName, Short usersAge, Short usersStatus, Integer noPromotionToken, String usersIdentityNo, String usersModifiedDate) {
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

    public PrivilegeEntity getFkPrivilegeId() {
        return fkPrivilegeId;
    }

    public Integer getNoPromotionToken() {
        return noPromotionToken;
    }

    public void setNoPromotionToken(Integer noPromotionToken) {
        this.noPromotionToken = noPromotionToken;
    }

    public String getUsersIdentityNo() {
        return usersIdentityNo;
    }

    public void setUsersIdentityNo(String usersIdentityNo) {
        this.usersIdentityNo = usersIdentityNo;
    }

    public Short getUsersStatus() {
        return usersStatus;
    }

    public void setUsersStatus(Short usersStatus) {
        this.usersStatus = usersStatus;
    }

    public Short getUsersAge() {
        return usersAge;
    }

    public void setUsersAge(Short usersAge) {
        this.usersAge = usersAge;
    }

    public String getUsersEmailAddress() {
        return usersEmailAddress;
    }

    public void setUsersEmailAddress(String usersEmailAddress) {
        this.usersEmailAddress = usersEmailAddress;
    }

    public String getUsersFullName() {
        return usersFullName;
    }

    public void setUsersFullName(String usersFullName) {
        this.usersFullName = usersFullName;
    }

    public String getUsersPassword() {
        return usersPassword;
    }

    public void setUsersPassword(String usersPassword) {
        this.usersPassword = usersPassword;
    }

    public String getUsersRegistrationDate() {
        return usersRegistrationDate;
    }

    public void setUsersRegistrationDate(String usersRegistrationDate) {
        this.usersRegistrationDate = usersRegistrationDate;
    }

    public String getUsersModifiedDate() {
        return usersModifiedDate;
    }

    public void setUsersModifiedDate(String usersModifiedDate) {
        this.usersModifiedDate = usersModifiedDate;
    }
}
