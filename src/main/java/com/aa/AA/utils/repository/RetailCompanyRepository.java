package com.aa.AA.utils.repository;

import com.aa.AA.entities.RetailCompanyEntity;
import com.aa.AA.entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface RetailCompanyRepository extends JpaRepository<RetailCompanyEntity,Long> {
    Optional<RetailCompanyEntity> findByLiquorStoreCertNo(String liquorStoreCertNo);
    Optional<RetailCompanyEntity> findByLiquorStoreName(String liquorStoreName);
    Optional<RetailCompanyEntity> findByOwnerName(UsersEntity usersEntity);
}
