package com.aa.AA.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class UsersRegistersPromotionTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pkUsersRegistersPromotionTypeId;

    @JoinColumn
    @OneToOne
    private UsersEntity usersEntity;

    @JoinColumn
    @OneToOne
    private RegistrationPromotionTypeEntity registrationPromotionTypeEntity;

    @JoinColumn
    @OneToOne
    private LiquorStoreEntity liquorStoreEntity;

    private LocalDateTime promotionRegisteredDate;
}
