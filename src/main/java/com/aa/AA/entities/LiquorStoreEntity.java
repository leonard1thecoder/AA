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

    @OneToMany(mappedBy = "entity", cascade= CascadeType.ALL)
    private List<LiquorStorePromotionsEntity> liquorStorePromotionsEntities;

    @OneToOne(mappedBy = "liquorStoreEntity", cascade= CascadeType.ALL)
    private UsersRegistersPromotionTypeEntity usersRegistersPromotionTypeEntity;

    @OneToMany(mappedBy = "liquorStoreEntity",cascade=CascadeType.ALL)
    private List<EventsEntity> eventsEntities;





    private String liquorStoreName,countryName,cityName,liquorStoreCertNo;

    private Byte liquorStoreStatus;

}
