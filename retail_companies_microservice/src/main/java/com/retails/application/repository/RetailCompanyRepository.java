package com.retails.application.repository;


import com.retails.application.entity.RetailCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface RetailCompanyRepository extends JpaRepository<RetailCompany,Long> {
    Optional<RetailCompany> findByRetailCompanyName(String liquorStoreName);
}
