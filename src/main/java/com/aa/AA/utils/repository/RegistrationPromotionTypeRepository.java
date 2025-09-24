package com.aa.AA.utils.repository;

import com.aa.AA.entities.RegistrationPromotionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface RegistrationPromotionTypeRepository extends JpaRepository<RegistrationPromotionTypeEntity,Byte> {
}
