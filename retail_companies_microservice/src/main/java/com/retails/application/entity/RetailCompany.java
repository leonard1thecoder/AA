package com.retails.application.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class RetailCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long fkUsersId;

    @Column(unique = true, nullable = false)
    private String registrationDate,modifiedDate;
    @Column(unique = true, nullable = false)
    private Byte fkPrivilegeId;

    @Column(unique = true, nullable = false)
    private String retailCompanyName, countryName, cityName, retailCompanyCertNo;

    @Column(unique = true, nullable = false)
    private Byte retailCompanyStatus;


}