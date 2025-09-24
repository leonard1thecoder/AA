package com.aa.AA.utils.repository;

import com.aa.AA.entities.LiquorStoreStockSizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component

public interface LiquorStoreStockSizeRepository extends JpaRepository<LiquorStoreStockSizeEntity,Short> {
}
