package com.privileges.application.repository;

import com.privileges.application.entity.Privileges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface PrivilegesRepository extends JpaRepository<Privileges,Integer> {
}
