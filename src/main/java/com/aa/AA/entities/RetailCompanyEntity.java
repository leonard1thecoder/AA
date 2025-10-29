package com.aa.AA.entities;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class RetailCompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long liquorStoreId;

    @ManyToOne
    @JoinColumn(name="pkUsersId", nullable = false)
    private UsersEntity usersEntity;

    @OneToOne
    @JoinColumn(name="pkPrivilegeId", nullable = false)
    private PrivilegeEntity privilegeEntity;

    private String liquorStoreName,countryName,cityName,liquorStoreCertNo;

    private Byte liquorStoreStatus;

    public RetailCompanyEntity(UsersEntity usersEntity, PrivilegeEntity privilegeEntity, String liquorStoreName, String countryName, String cityName, String liquorStoreCertNo, Byte liquorStoreStatus) {
        this.usersEntity = usersEntity;
        this.privilegeEntity = privilegeEntity;
        this.liquorStoreName = liquorStoreName;
        this.countryName = countryName;
        this.cityName = cityName;
        this.liquorStoreCertNo = liquorStoreCertNo;
        this.liquorStoreStatus = liquorStoreStatus;
    }
}
