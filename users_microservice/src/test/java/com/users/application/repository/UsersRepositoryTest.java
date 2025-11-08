package com.users.application.repository;

import com.users.application.entities.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)

@DataJpaTest
class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TestEntityManager tem;


    @Test
    void findByUserIdentityNo() {
        //when

       Users user = tem.persist(new Users());

        //given
       var entity = usersRepository.findByUserIdentityNo(user.getUserIdentityNo());
       
       //assert
    }

    @Test
    void findByUserFullName() {
    }

    @Test
    void findByUserEmailAddress() {
    }

    @Test
    void findByUserEmailAddressAndUserPassword() {
    }
}