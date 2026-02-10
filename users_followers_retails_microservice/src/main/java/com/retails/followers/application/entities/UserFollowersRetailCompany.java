package com.retails.followers.application.entities;

import com.retails.application.entity.RetailCompany;
import com.users.application.entities.Users;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowersRetailCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private RetailCompany companyRetail;

    @Column(nullable = false)
    private String registeredFollowDate;

    @Column
    private String registeredUnfollowingDate;

    @Column(nullable = false)
    private Byte status;
}
