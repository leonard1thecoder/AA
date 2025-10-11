package com.aa.AA.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@ToString
@NoArgsConstructor


/*
  * Once promotion deployed, need to change the uses liquor store
 */
public class UsersEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long usersId;
    @JoinColumn(name = "pkPrivilegeId", nullable = true)
    @OneToOne
    private PrivilegeEntity fkPrivilegeId;

    private Long usersIdentityNo;
    /*
        NB!!! All type of promotion need separate fields
     */
    private Integer noPromotionToken;
    private Short usersStatus, usersAge;
    private String usersFullName, usersEmailAddress, usersPassword, usersRegistrationDate, usersModifiedDate;

    private String token;

    public Long getUsersId() {
        return usersId;
    }

    public void setUsersId(Long usersId) {
        this.usersId = usersId;
    }

    public PrivilegeEntity getFkPrivilegeId() {
        return fkPrivilegeId;
    }

    public void setFkPrivilegeId(PrivilegeEntity fkPrivilegeId) {
        this.fkPrivilegeId = fkPrivilegeId;
    }

    public Long getUsersIdentityNo() {
        return usersIdentityNo;
    }

    public void setUsersIdentityNo(Long usersIdentityNo) {
        this.usersIdentityNo = usersIdentityNo;
    }

    public Integer getNoPromotionToken() {
        return noPromotionToken;
    }

    public void setNoPromotionToken(Integer noPromotionToken) {
        this.noPromotionToken = noPromotionToken;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString(){
        return this.usersFullName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(fkPrivilegeId.privilegeName()));
    }

    @Override
    public String getPassword() {
        return this.getUsersPassword();
    }

    @Override
    public String getUsername() {
        return this.getUsersEmailAddress();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
