package com.aa.AA.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@ToString
@Setter
@Getter
@NoArgsConstructor
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pkUsersId;
    private Long usersIdentityNo;
    private Integer fkPrivilegeId;
    private Short usersStatus,usersAge;
    private String usersFullName, usersEmailAddress, usersPassword, usersCountryName, usersRegistrationDate, usersLanguage, usersModifiedDate;

    public Long getUsersIdentityNo() {
        return usersIdentityNo;
    }

    public Short getUsersAge() {
        return usersAge;
    }
}
