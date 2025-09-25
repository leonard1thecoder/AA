package com.aa.AA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@ToString

@NoArgsConstructor


/*
  * Once promotion deployed, need to change the uses liquor store
 */
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(insertable=false, updatable=false)
    private Long pkUsersId;
    @JoinColumn(name = "pkPrivilegeId", nullable = true)
    @OneToOne
    private PrivilegeEntity fkPrivilegeId;

    @JoinColumn(name = "code")
    @OneToOne
    private Country countryEntity;

    @JoinColumn(name = "cityId")
    @OneToOne
    private City city;


    @OneToOne
    @JoinColumn(name= "pkPromotionId")
    private  LiquorStorePromotionsEntity liquorStorePromotionsEntity;
//    @OneToMany(mappedBy = "usersEntity", cascade = CascadeType.ALL)
//    private List<AlcoholAgentEntity> alcoholAgentEntities;
//
//    @OneToMany(mappedBy = "usersEntity", cascade = CascadeType.ALL)
//    private List<LiquorStoreEntity> liquorStoreEntities;
//
//    @ManyToMany(mappedBy = "usersEntity", cascade = CascadeType.ALL)
//    private List<LiquorStoreStockEntity> liquorStoreStockEntities;
//
//    @OneToMany(mappedBy = "usersEntity")
//    private List<LiquorStorePromotionUsersEntity> liquorStorePromotionUsersEntities;
//
//    @OneToOne(mappedBy = "usersEntity")
//    private UsersRegistersPromotionTypeEntity usersRegistersPromotionTypeEntity;
//
//    @OneToMany(mappedBy="usersEntity", cascade = CascadeType.ALL)
//    private List<EventAttendanceEntity> eventAttendanceEntities;

    private Long usersIdentityNo;
    /*
        NB!!! All type of promotion need separate fields
     */
    private Integer noPromotionToken;
    private Short usersStatus, usersAge;
    private String usersFullName, usersEmailAddress, usersPassword, usersRegistrationDate, usersModifiedDate;

    public Long getUsersIdentityNo() {
        return usersIdentityNo;
    }

    public Short getUsersAge() {
        return usersAge;
    }
}
