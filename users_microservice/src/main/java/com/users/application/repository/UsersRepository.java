package com.users.application.repository;

import com.users.application.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UsersRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByUserIdentityNo(String userIdentityNo);

    Optional<Users> findByUserFullName(String userFullName);

    Optional<Users> findByUserEmailAddress(String userEmailAddress);

    Optional<Users> findByUserEmailAddressAndUserPassword(String userEmailAddress, String userPassword);
}
