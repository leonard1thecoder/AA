package com.aa.AA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    private String promotionName;

    private LocalDateTime promotionCreationDate,promotionExpirationDate;

    private Byte promotionStatus;

}
