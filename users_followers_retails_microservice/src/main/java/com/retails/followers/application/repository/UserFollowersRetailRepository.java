package com.retails.followers.application.repository;

import com.retails.followers.application.entities.UserFollowersRetailCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFollowersRetailRepository extends JpaRepository<UserFollowersRetailCompany,Long> {
    Optional<UserFollowersRetailCompany>
    findByUserIdAndCompanyRetailId(Long userId, Long companyId);

    List<UserFollowersRetailCompany> findByUserId(Long userId);

    List<UserFollowersRetailCompany> findByCompanyRetailId(Long companyId);}
