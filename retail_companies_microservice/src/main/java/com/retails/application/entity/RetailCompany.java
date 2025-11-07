package com.retails.application.entity;

import com.privileges.application.entity.Privileges;
import com.users.application.entities.Users;
import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Setter
@Entity
public class RetailCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="pkUsersId", nullable = false)
    private Users usersEntity;

    @OneToOne
    @JoinColumn(name="pkPrivilegeId", nullable = false)
    private Privileges privilegeEntity;

    private String liquorStoreName,countryName,cityName,liquorStoreCertNo;

    private Byte liquorStoreStatus;


   }
