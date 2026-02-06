package com.privileges.application.repository;

import com.privileges.application.entity.Privileges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface PrivilegesRepository extends JpaRepository<Privileges,Integer> {
    Optional<Privileges> findByPrivilegeName(String privilegeName);
}
