package com.aa.AA.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class AlcoholAgentEntity {


    @Id
    @GeneratedValue
    private Long pkAlcoholAgentId;

    @JoinColumn(name = "pkUsersId", nullable = true)
    @OneToOne
    private UsersEntity usersEntity;

    @JoinColumn(name = "pkStockId", nullable = true)
    @OneToMany
    private List<LiquorStoreStockEntity> liquorStoreStockEntity;

    private Integer itemsPurchase;

    private Double purchasePrice;

    private Integer hasBottleStatus,alcoholAgentStatusByUser,purchaseStatusByAgent;

}
