package com.aa.AA.dtos;

import com.aa.AA.entities.PrivilegeEntity;
import com.aa.AA.entities.UsersEntity;


public class RetailCompanyResponse {

    private Long liquorStoreId;


    private UsersEntity usersEntity;


    private PrivilegeEntity privilegeEntity;

    private String liquorStoreName,countryName,cityName,liquorStoreCertNo;

    private Byte liquorStoreStatus;

    public RetailCompanyResponse(Long liquorStoreId, PrivilegeEntity privilegeEntity, UsersEntity usersEntity, String liquorStoreName, String countryName, String cityName, String liquorStoreCertNo, Byte liquorStoreStatus) {
        this.liquorStoreId = liquorStoreId;
        this.privilegeEntity = privilegeEntity;
        this.usersEntity = usersEntity;
        this.liquorStoreName = liquorStoreName;
        this.countryName = countryName;
        this.cityName = cityName;
        this.liquorStoreCertNo = liquorStoreCertNo;
        this.liquorStoreStatus = liquorStoreStatus;
    }

    public Long getLiquorStoreId() {
        return liquorStoreId;
    }

    public void setLiquorStoreId(Long liquorStoreId) {
        this.liquorStoreId = liquorStoreId;
    }

    public PrivilegeEntity getPrivilegeEntity() {
        return privilegeEntity;
    }

    public void setPrivilegeEntity(PrivilegeEntity privilegeEntity) {
        this.privilegeEntity = privilegeEntity;
    }

    public UsersEntity getUsersEntity() {
        return usersEntity;
    }

    public void setUsersEntity(UsersEntity usersEntity) {
        this.usersEntity = usersEntity;
    }

    public String getLiquorStoreName() {
        return liquorStoreName;
    }

    public void setLiquorStoreName(String liquorStoreName) {
        this.liquorStoreName = liquorStoreName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
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
