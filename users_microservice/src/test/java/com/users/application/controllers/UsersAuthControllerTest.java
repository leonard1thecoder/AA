package com.users.application.controllers;

import com.users.application.dtos.LoginRequest;
import com.users.application.dtos.UsersRegisterRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class UsersAuthControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    class TestLogin{
        @Test
        void testLoginMethod_validLoginCredentials() {
        //When
            LoginRequest request = LoginRequest
                    .builder()
                    .usersEmailAddress("em2ail2@email.com")
                    .usersPassword("12345")
                    .build();


            //Given.
            var response =
                    restTemplate.postForEntity("/dev/api/auth/login", request, List.class);

//Assert
            System.out.println(response.getBody());
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());

        }

        @Test
        void testLoginMethod_invalidLoginEmailAddress() {
            //When
            LoginRequest request = LoginRequest
                    .builder()
                    .usersEmailAddress("em222ail2@email.com")
                    .usersPassword("12345")
                    .build();


            //Given.
            var response =
                    restTemplate.postForEntity("/dev/api/auth/login", request, List.class);

//Assert
            System.out.println(response.getBody());
            assertEquals(HttpStatus.NOT_FOUND , response.getStatusCode());
            assertNotNull(response.getBody());

        }

        @Test
        void testLoginMethod_invalidLoginPassword() {
            //When
            LoginRequest request = LoginRequest
                    .builder()
                    .usersEmailAddress("em22ail2@email.com")
                    .usersPassword("123455")
                    .build();


            //Given.
            var response =
                    restTemplate.postForEntity("/dev/api/auth/login", request, List.class);

//Assert
            System.out.println(response.getBody());
            assertEquals(HttpStatus.BAD_REQUEST , response.getStatusCode());
            assertNotNull(response.getBody());
        }
    }

    @Nested
    class TestUsersRegister {

        @Test
        void testUserRegistersMethod_validRegistrationInfo() {
            //When
            UsersRegisterRequest request = UsersRegisterRequest
                    .builder()
                    .userCellphoneNo("0846337773")
                    .userStatus((short)0)
                    .userPassword("#123Abc9")
                    .userIdentityNo("9709225454083")
                    .userFullName("Sizolw Mthimunye")
                    .userEmailAddress("leo111thecoder@gmail.com")
                    .privileges(1)
                    .build();

            //Given
            var response =
                    restTemplate.postForEntity("/dev/api/auth/register", request, List.class);


            //Assert
            assertEquals(HttpStatus.CREATED , response.getStatusCode());


        }

        @Test
        void testUserRegistersMethod_userAlreadyRegisteredAndStatus0() {
            //When
            UsersRegisterRequest request = UsersRegisterRequest
                    .builder()
                    .userCellphoneNo("0846337773")
                    .userStatus((short)0)
                    .userPassword("#123Abc9")
                    .userIdentityNo("9709225454083")
                    .userFullName("Sizolw Mthimunye")
                    .userEmailAddress("leo111thecoder@gmail.com")
                    .privileges(1)
                    .build();

            //Given
            var response =
                    restTemplate.postForEntity("/dev/api/auth/register", request, List.class);


            //Assert
            assertEquals(HttpStatus.FORBIDDEN , response.getStatusCode());
        }

        @Test
        void testUserRegistersMethod_userAlreadyRegisteredAndStatus1() {
            //When
            UsersRegisterRequest request = UsersRegisterRequest
                    .builder()
                    .userCellphoneNo("0846337773")
                    .userStatus((short)0)
                    .userPassword("#123Abc9")
                    .userIdentityNo("9709225454083")
                    .userFullName("Sizolw Mthimunye")
                    .userEmailAddress("leo11thecoder@gmail.com")
                    .privileges(1)
                    .build();

            //Given
            var response =
                    restTemplate.postForEntity("/dev/api/auth/register", request, List.class);


            //Assert
            assertEquals(HttpStatus.FORBIDDEN , response.getStatusCode());

        }




        }

    @Test
    void updateUsersPassword() {
    }
}