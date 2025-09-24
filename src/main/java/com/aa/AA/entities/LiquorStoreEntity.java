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
public class LiquorStoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pkLiquorStoreId;

    @ManyToOne
    @JoinColumn(name="pkUsersId", nullable = false)
    private UsersEntity usersEntity;

    @OneToOne
    @JoinColumn(name="pkPrivilegeId", nullable = false)
    private PrivilegeEntity privilegeEntity;

    @OneToMany(mappedBy = "liquorStoreEntity",cascade = CascadeType.ALL)
    private List<LiquorStoreStockEntity> liquorStoreStockEntities;

    @OneToMany(mappedBy = "liquorStoreEntity", cascade= CascadeType.ALL)
    private List<LiquorStorePromotionsEntity> liquorStorePromotionsEntities;

    @OneToOne(mappedBy = "liquorStoreEntity", cascade= CascadeType.ALL)
    private UsersRegistersPromotionTypeEntity usersRegistersPromotionTypeEntity;

    @OneToMany(mappedBy = "liquorStoreEntity",cascade=CascadeType.ALL)
    private List<EventsEntity> eventsEntities;

    @JoinColumn(name = "code")
    @OneToOne
    private Country countryEntity;

    @JoinColumn(name = "cityId")
    @OneToOne
    private City city;

    private String liquorStoreName,liquorStoreCertNo;

    private Byte liquorStoreStatus;

}
