package com.aa.AA.utils.repository;

import com.aa.AA.entities.LiquorStoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface LiquorStoreRepository extends JpaRepository<LiquorStoreEntity,Long> {
}
