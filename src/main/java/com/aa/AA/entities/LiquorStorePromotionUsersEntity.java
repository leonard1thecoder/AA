package com.aa.AA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany
    @JoinColumn(name = "pkPromotionId", nullable = true)
    private List<LiquorStorePromotionsEntity> liquorStorePromotionsEntities;

    @JoinColumn(name = "pkUsersId", nullable = true)
    @ManyToOne
    private UsersEntity usersEntity;

    private LocalDateTime promotionUsedDate;

    private Byte promotionUsedStatus;


}
