package com.aa.AA.entities;

import jakarta.persistence.*;
import lombok.*;

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

    private Long usersIdentityNo;
    private Short usersStatus,usersAge;
    private String usersFullName, usersEmailAddress, usersPassword, usersCountryName, usersRegistrationDate, usersLanguage, usersModifiedDate;

    public Long getUsersIdentityNo() {
        return usersIdentityNo;
    }

    public Short getUsersAge() {
        return usersAge;
    }
}
