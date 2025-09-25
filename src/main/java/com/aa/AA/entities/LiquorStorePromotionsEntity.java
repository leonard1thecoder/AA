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
public class LiquorStorePromotionsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pkPromotionId;

    @JoinColumn
    @OneToMany
    private List<LiquorStoreEntity> entity;

    @JoinColumn
    @OneToOne
    private LiquorStorePromotionsTypeEntity liquorStorePromotionsTypeEntity;

//    @OneToMany(mappedBy = "liquorStorePromotionsEntities",cascade = CascadeType.ALL)
//    private List<LiquorStorePromotionUsersEntity> liquorStorePromotionUsersEntities;
//
//    @OneToMany(mappedBy = "liquorStorePromotionsEntity",cascade = CascadeType.ALL)
//    private  List<UsersEntity> usersEntities;

    private String promotionName, promotionDescription;

    private LocalDateTime promotionCreationDate, promotionExpirationDate;


    private Byte numberOfPromotion,promotionStatus;

}
