package com.aa.AA.entities;


import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class AlcoholAgentEntity {


    @Id
    @GeneratedValue
    private Long alcoholAgentId;

    @JoinColumn(name = "pkUsersId", nullable = false)
    @OneToOne
    private UsersEntity usersEntity;

    @JoinColumn(name = "pkStockId", nullable = false)
    @ManyToOne
    private RetailCompanyStockEntity retailCompanyStockEntity;

    private Integer itemsPurchase;

    private Double purchasePrice;

    private Integer hasBottleStatus,alcoholAgentStatusByUser,purchaseStatusByAgent;

}
