package com.aa.AA.utils.repository;

import com.aa.AA.entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface UsersRepository extends JpaRepository<UsersEntity,Long> {

    List<UsersEntity> findByUsersIdentityNo(Long usersIdentityNo);

    List<UsersEntity> findByUsersFullName(String usersFullName);

    Optional<UsersEntity> findByUsersEmailAddress(String usersEmailAddress);

    List<UsersEntity> findByUsersEmailAddressAndUsersPassword(String usersEmailAddress, String usersPassword);
}
