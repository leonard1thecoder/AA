package com.users.application.repository;


import com.users.application.entities.Users;
import jakarta.transaction.Transactional;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)

@DataJpaTest
class UsersRepositoryTest {
    private static Logger logger = LoggerFactory.getLogger(UsersRepositoryTest.class);

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TestEntityManager tem;

    private Users user;

    @BeforeEach
    public void persist() {
        user = tem.persist(Users.builder()
                .userAge((short) 30)
                .userCellphoneNo("0788725439")
                .userFullName("Sizolwakhe Leonard Mthimunye")
                .userPassword("12345")
                .userModifiedDate(LocalDateTime.now().toString())
                .userRegistrationDate(LocalDateTime.now().toString())
                .userStatus((short) 0)
                .userEmailAddress("localhost@gmail.com")
                .fk_privilege_id(1)
                .userIdentityNo("97112259743083")
                .build());
    }

    @AfterEach
    public void detach(){
        tem.clear();
    }

    @Transactional
    @Test
    void testFindByUserIdentityNo_ExistingUserIdentityNo() {
        //when
        var savedEntity = usersRepository.save(user);

        //given
        var entity = usersRepository.findByUserIdentityNo("97112259743083");
        logger.info("entity saved: {}\n, entity got: {} ", savedEntity, entity);
        //assert
        Assert.assertTrue(entity.isPresent());
        Assert.assertEquals(savedEntity.getUserIdentityNo(), entity.get().getUserIdentityNo());
    }

    @Transactional
    @Test
    void testFindByUserIdentityNo_NonExistingUserIdentityNo() {
        //when
        usersRepository.save(user);

        //given
        var entity = usersRepository.findByUserIdentityNo("9711527553083");
        //assert
        Assertions.assertTrue(entity.isEmpty());
    }

    @Test
    void testFindByUserFullName_ExistingFullName() {
        //when
        var savedEntity = usersRepository.save(user);
        //given
        var entity = usersRepository.findByUserFullName("Sizolwakhe Leonard Mthimunye");

        //assert
        Assertions.assertTrue(entity.isPresent());
        Assertions.assertEquals(savedEntity.getUserFullName(),entity.get().getUserFullName());
    }

    @Test
    void testFindByUserFullName_NonExistingFullName() {
        //when
        var savedEntity = usersRepository.save(user);
        //given
        var entity = usersRepository.findByUserFullName("John Leonard Mthimunye");

        //assert
        Assertions.assertTrue(entity.isEmpty());
    }

    @Test
    void testFindByUserEmailAddress_ExistingEmail() {
        //when
        var savedEntity = usersRepository.save(user);
        //given
        var entity = usersRepository.findByUserEmailAddress("localhost@gmail.com");

        //assert
        Assertions.assertTrue(entity.isPresent());
    }

    @Test
    void testFindByUserEmailAddress_NonExistingEmailAddress() {
        //when
        var savedEntity = usersRepository.save(user);
        //given
        var entity = usersRepository.findByUserEmailAddress("localhost@gmail.com");

        //assert
        Assertions.assertTrue(entity.isPresent());
    }

    @Test
    void findByUserEmailAddressAndUserPassword_NonExistingUserEmailAddressAndExistingUserPassword() {
        //when
        var savedEntity = usersRepository.save(user);
        //given
        var entity = usersRepository.findByUserEmailAddressAndUserPassword("testlocalhost@gmail.com","12345");

        //assert
        Assertions.assertTrue(entity.isEmpty());
    }

    @Test
    void findByUserEmailAddressAndUserPassword_ExistingUserEmailAddressAndExistingUserPassword() {
        //when
        var savedEntity = usersRepository.save(user);
        //given
        var entity = usersRepository.findByUserEmailAddressAndUserPassword("localhost@gmail.com","12345");

        //assert
        Assertions.assertTrue(entity.isPresent());
    }

    @Test
    void testFindByUserFullName_ExistingCellphone() {
        //when
        var savedEntity = usersRepository.save(user);
        //given
        var entity = usersRepository.findByUserCellphoneNo("0788725439");

        //assert
        Assertions.assertTrue(entity.isPresent());
        Assertions.assertEquals(savedEntity.getUserFullName(),entity.get().getUserFullName());
    }

    @Test
    void testFindByUserFullName_NonCellphoneNu() {
        //when
        var savedEntity = usersRepository.save(user);
        //given
        var entity = usersRepository.findByUserCellphoneNo("0755375");

        //assert
        Assertions.assertTrue(entity.isEmpty());
    }
}