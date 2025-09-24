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
public class LiquorStorePromotionUsersEntity {

    @Id
    @GeneratedValue
    private Long pkPromotionUsersId;

    @ManyToMany
    @JoinColumn(name = "pkPromotionId", nullable = false)
    private LiquorStorePromotionsEntity liquorStorePromotionsEntities;

    @JoinColumn(name = "pkUsersId", nullable = false)
    @ManyToOne
    private UsersEntity usersEntity;

    private LocalDateTime promotionUsedDate;

    private Byte promotionUsedStatus;


}
