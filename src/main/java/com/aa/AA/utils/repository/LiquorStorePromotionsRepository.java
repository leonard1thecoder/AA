package com.aa.AA.utils.repository;

import com.aa.AA.entities.LiquorStorePromotionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface LiquorStorePromotionsRepository extends JpaRepository<LiquorStorePromotionsEntity,Long> {
}
