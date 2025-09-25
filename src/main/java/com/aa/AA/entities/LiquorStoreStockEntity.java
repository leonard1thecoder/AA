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
public class LiquorStoreStockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pkStockId;

    @ManyToOne
    @JoinColumn(name="pkUsersId", nullable =false)
    private UsersEntity usersEntity;

    @OneToOne
    @JoinColumn(name="pkPrivilegeId", nullable= false)
    private PrivilegeEntity privilegeEntity;

    @OneToOne
    @JoinColumn(name="pkStockSizeId", nullable = false)
    private LiquorStoreStockSizeEntity liquorStoreStockSize;

    @ManyToOne
    @JoinColumn(name="pkLiquorStoreId", nullable = false)
    private LiquorStoreEntity liquorStoreEntity;

    @OneToMany(mappedBy = "liquorStoreStockEntity", cascade = CascadeType.ALL)
    private List<AlcoholAgentEntity> alcoholAgentEntities;

    private LocalDateTime stockRegistrationDate, stockModifiedDate;

    private String stockBrandName;

    private Long stockNumber;

    private  Byte stockStatus;

    private Double stockPrice,stockLatePrice;


}
