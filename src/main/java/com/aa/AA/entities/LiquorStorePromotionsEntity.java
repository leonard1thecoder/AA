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
public class LiquorStorePromotionsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pkPromotionId;

    @JoinColumn
    @ManyToMany
    private LiquorStoreEntity entity;

    @OneToMany(mappedBy = "liquorStorePromotionsEntities")
    private List<LiquorStorePromotionUsersEntity> liquorStorePromotionUsersEntities;

    private String promotionName;

    private LocalDateTime promotionCreationDate, promotionExpirationDate;

    private Byte promotionStatus;

}
