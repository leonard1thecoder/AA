package com.aa.AA.utils.repository;

import com.aa.AA.entities.RegistrationPromotionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface RegistrationPromotionTypeRepository extends JpaRepository<RegistrationPromotionType,Byte> {
}
