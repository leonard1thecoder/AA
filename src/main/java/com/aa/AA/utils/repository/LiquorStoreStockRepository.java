package com.aa.AA.utils.repository;

import com.aa.AA.entities.RetailCompanyStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component

public interface LiquorStoreStockRepository extends JpaRepository<RetailCompanyStockEntity,Long> {
}
