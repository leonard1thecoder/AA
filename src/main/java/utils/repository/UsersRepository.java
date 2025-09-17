package utils.repository;

import entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UsersRepository extends JpaRepository<UsersEntity,Long> {


}
