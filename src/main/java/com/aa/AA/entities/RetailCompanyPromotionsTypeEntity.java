package com.aa.AA.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class RetailCompanyPromotionsTypeEntity {

    @Id
    @GeneratedValue
    private Byte promotionTypeId;

    private String promotionTypeName;

}
