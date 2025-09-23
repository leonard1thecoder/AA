package com.aa.AA.utils.repository;

import com.aa.AA.entities.PrivilegeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface PrivilegesRepository extends JpaRepository<PrivilegeEntity,Integer> {
}
