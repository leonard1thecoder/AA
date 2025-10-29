package com.aa.AA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Entity


/*
Promotion need to filter by event sTE
    * No of purchase to certain product, get certain percentage discount
    * First Number of people to select upcoming Artist
    * First Number of people to select to come in event and come !!! People will promotion in event
    *
 * */
public class RetailCompanyPromotionsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long promotionId;

    @JoinColumn(name = "pkLiquorStoreId")
    @ManyToOne
    private RetailCompanyEntity entity;

    @JoinColumn(name="pkPromotionTypeId", nullable = false)
    @OneToOne
    private RetailCompanyPromotionsTypeEntity retailCompanyPromotionsTypeEntity;

    @OneToMany(mappedBy = "liquorStorePromotionsEntities",cascade = CascadeType.ALL)
    private List<RetailCompanyPromotionUsersEntity> liquorStorePromotionUsersEntities;

    private String promotionName, promotionDescription;

    private LocalDateTime promotionCreationDate, promotionExpirationDate;


    private Byte numberOfPromotion,promotionStatus;

}
