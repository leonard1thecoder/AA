package utils.repository;

import entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UsersRepository extends JpaRepository<UsersEntity,Long> {

    List<UsersEntity> findByUsersIdentityNo(Long usersIdentityNo);

    List<UsersEntity> findByUsersFullName(String usersFullName);
}
