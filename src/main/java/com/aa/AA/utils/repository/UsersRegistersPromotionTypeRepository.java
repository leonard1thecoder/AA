package com.aa.AA.utils.repository;

import com.aa.AA.entities.UsersRegistersPromotionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UsersRegistersPromotionTypeRepository extends JpaRepository<UsersRegistersPromotionTypeEntity,Long> {
}
