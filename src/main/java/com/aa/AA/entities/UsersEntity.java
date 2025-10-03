package com.aa.AA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@ToString
@NoArgsConstructor


/*
  * Once promotion deployed, need to change the uses liquor store
 */
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(insertable=false, updatable=false)
    private Long pkUsersId;
    @JoinColumn(name = "pkPrivilegeId", nullable = true)
    @OneToOne
    private PrivilegeEntity fkPrivilegeId;
    @Column(nullable = false)
    private Long usersIdentityNo;
    /*
        NB!!! All type of promotion need separate fields
     */
    @Column(nullable = false)
    private Integer noPromotionToken;
    @Column(nullable = false)
    private Short usersStatus, usersAge;
    @Column(nullable = false)
    private String usersFullName, usersEmailAddress, usersPassword, usersRegistrationDate, usersModifiedDate;

    public Long getUsersIdentityNo() {
        return usersIdentityNo;
    }

    public Short getUsersAge() {
        return usersAge;
    }

    public Long getPkUsersId() {
        return pkUsersId;
    }

    public void setPkUsersId(Long pkUsersId) {
        this.pkUsersId = pkUsersId;
    }

    public PrivilegeEntity getFkPrivilegeId() {
        return fkPrivilegeId;
    }

    public void setFkPrivilegeId(PrivilegeEntity fkPrivilegeId) {
        this.fkPrivilegeId = fkPrivilegeId;
    }

    public Integer getNoPromotionToken() {
        return noPromotionToken;
    }

    public void setNoPromotionToken(Integer noPromotionToken) {
        this.noPromotionToken = noPromotionToken;
    }

    public void setUsersIdentityNo(Long usersIdentityNo) {
        this.usersIdentityNo = usersIdentityNo;
    }

    public Short getUsersStatus() {
        return usersStatus;
    }

    public void setUsersStatus(Short usersStatus) {
        this.usersStatus = usersStatus;
    }

    public void setUsersAge(Short usersAge) {
        this.usersAge = usersAge;
    }

    public String getUsersFullName() {
        return usersFullName;
    }

    public void setUsersFullName(String usersFullName) {
        this.usersFullName = usersFullName;
    }

    public String getUsersEmailAddress() {
        return usersEmailAddress;
    }

    public void setUsersEmailAddress(String usersEmailAddress) {
        this.usersEmailAddress = usersEmailAddress;
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

    @Override
    public String toString(){
        return this.usersFullName;
    }
}
