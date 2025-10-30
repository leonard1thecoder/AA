package com.aa.AA.entities;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Setter
@Entity
public class RetailCompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="pkUsersId", nullable = false)
    private UsersEntity usersEntity;

    @OneToOne
    @JoinColumn(name="pkPrivilegeId", nullable = false)
    private PrivilegeEntity privilegeEntity;

    private String liquorStoreName,countryName,cityName,liquorStoreCertNo;

    private Byte liquorStoreStatus;

    public RetailCompanyEntity(Long id, UsersEntity usersEntity, PrivilegeEntity privilegeEntity, String liquorStoreName, String countryName, String cityName, String liquorStoreCertNo, Byte liquorStoreStatus) {
        this.id = id;
        this.usersEntity = usersEntity;
        this.privilegeEntity = privilegeEntity;
        this.liquorStoreName = liquorStoreName;
        this.countryName = countryName;
        this.cityName = cityName;
        this.liquorStoreCertNo = liquorStoreCertNo;
        this.liquorStoreStatus = liquorStoreStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsersEntity getUsersEntity() {
        return usersEntity;
    }

    public void setUsersEntity(UsersEntity usersEntity) {
        this.usersEntity = usersEntity;
    }

    public PrivilegeEntity getPrivilegeEntity() {
        return privilegeEntity;
    }

    public void setPrivilegeEntity(PrivilegeEntity privilegeEntity) {
        this.privilegeEntity = privilegeEntity;
    }

    public String getLiquorStoreName() {
        return liquorStoreName;
    }

    public void setLiquorStoreName(String liquorStoreName) {
        this.liquorStoreName = liquorStoreName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getLiquorStoreCertNo() {
        return liquorStoreCertNo;
    }

    public void setLiquorStoreCertNo(String liquorStoreCertNo) {
        this.liquorStoreCertNo = liquorStoreCertNo;
    }

    public Byte getLiquorStoreStatus() {
        return liquorStoreStatus;
    }

    public void setLiquorStoreStatus(Byte liquorStoreStatus) {
        this.liquorStoreStatus = liquorStoreStatus;
    }
}
