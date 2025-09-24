package com.aa.AA.entities;

import jakarta.persistence.*;
import lombok.*;

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

    private String liquorStoreName,liquorStoreCertNo,liquorStoreCountry,liquorCity;

    private Byte liquorStoreStatus;



}
