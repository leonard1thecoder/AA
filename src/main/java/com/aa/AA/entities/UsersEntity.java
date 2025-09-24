package com.aa.AA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@ToString

@NoArgsConstructor
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long pkUsersId;
    @JoinColumn(name = "pkPrivilegeId", nullable = false)
    @OneToOne(cascade = CascadeType.ALL)
    private PrivilegeEntity fkPrivilegeId;

    @JoinColumn(name = "code")
    @OneToOne
    private Country countryEntity;

    @JoinColumn(name = "cityId")
    @OneToOne
    private City city;


    @OneToMany(mappedBy = "usersEntity", cascade = CascadeType.ALL)
    private List<LiquorStoreEntity> liquorStoreEntities;

    @ManyToMany(mappedBy = "usersEntity", cascade = CascadeType.ALL)
    private List<LiquorStoreStockEntity> liquorStoreStockEntities;

    @OneToMany(mappedBy = "usersEntity")
    private List<LiquorStorePromotionUsersEntity> liquorStorePromotionUsersEntities;

    @OneToOne(mappedBy = "usersEntity")
    private UsersRegistersPromotionTypeEntity usersRegistersPromotionTypeEntity;

    @OneToMany(mappedBy="usersEntity", cascade = CascadeType.ALL)
    private List<EventAttendanceEntity> eventAttendanceEntites;

    private Long usersIdentityNo;
    private Short usersStatus, usersAge;
    private String usersFullName, usersEmailAddress, usersPassword, usersRegistrationDate, usersModifiedDate;

    public Long getUsersIdentityNo() {
        return usersIdentityNo;
    }

    public Short getUsersAge() {
        return usersAge;
    }
}
