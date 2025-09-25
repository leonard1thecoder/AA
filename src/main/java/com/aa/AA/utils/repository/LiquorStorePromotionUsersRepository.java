package com.aa.AA.utils.repository;

import com.aa.AA.entities.LiquorStorePromotionUsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component

public interface LiquorStorePromotionUsersRepository extends JpaRepository<LiquorStorePromotionUsersEntity,Long> {
}
