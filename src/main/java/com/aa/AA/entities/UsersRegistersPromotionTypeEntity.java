package com.aa.AA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class UsersRegistersPromotionTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long usersRegistersPromotionTypeId;

    @JoinColumn(name="pkUsersId", nullable = true)
    @OneToOne
    private UsersEntity usersEntity;

    @JoinColumn(name="pkRegistrationPromotionTypeId", nullable = true)
    @OneToOne
    private RegistrationPromotionTypeEntity registrationPromotionTypeEntity;

    @JoinColumn(name="pkLiquorStoreId")
    @OneToOne
    private RetailCompanyEntity retailCompanyEntity;

    private LocalDateTime promotionRegisteredDate;
}
