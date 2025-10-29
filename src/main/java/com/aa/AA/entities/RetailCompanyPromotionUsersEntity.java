package com.aa.AA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RetailCompanyPromotionUsersEntity {

    @Id
    @GeneratedValue
    private Long promotionUsersId;

    @ManyToOne
    @JoinColumn(name = "pkPromotionId", nullable = false)
    private RetailCompanyPromotionsEntity liquorStorePromotionsEntities;

    @JoinColumn(name = "pkUsersId", nullable = true)
    @ManyToOne
    private UsersEntity usersEntity;

    private LocalDateTime promotionUsedDate;

    private Byte promotionUsedStatus;


}
