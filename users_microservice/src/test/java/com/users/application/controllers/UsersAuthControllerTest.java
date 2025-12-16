package com.users.application.controllers;

import com.users.application.dtos.LoginRequest;
import com.users.application.dtos.UpdatePasswordRequest;
import com.users.application.dtos.UsersRegisterRequest;
import com.users.application.dtos.UsersResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

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


        @Nested
        class TestUsersUpdatePassword{

            @Test
            void updateUsersPasswordMethod_usersEmailDoNotExists() {

                UpdatePasswordRequest request =UpdatePasswordRequest
                        .builder()
                        .usersPassword("12345")
                        .usersConfirmPassword("12345")
                        .usersEmailAddress("blueStar@mail.com")
                        .build();

                //Given



                var response =
                        restTemplate.exchange("/dev/api/auth/updatePassword", HttpMethod.PUT, sendRequestANdGetResponse(request), List.class);


                assertEquals(HttpStatus.NOT_FOUND , response.getStatusCode());

            }

            private HttpEntity sendRequestANdGetResponse(UpdatePasswordRequest request){
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                return new HttpEntity<>(request, headers);
            }

            @Test
            void testUpdatePasswordMethod_validateEmailAddress(){
             var request = UpdatePasswordRequest
                     .builder()
                     .usersEmailAddress("leo11thecoder@gmail.com")
                     .usersPassword("#KingSparkon1")
                     .usersConfirmPassword("#KingSparkon1")
                     .build();


                var response =
                        restTemplate.exchange("/dev/api/auth/updatePassword", HttpMethod.PUT, sendRequestANdGetResponse(request), List.class);


                Assertions.assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
            }

            @Test
            void testUpdatePasswordMethod_misMatchPassword(){
                var request = UpdatePasswordRequest
                        .builder()
                        .usersEmailAddress("leo11thecoder@gmail.com")
                        .usersPassword("#KingSparkon21")
                        .usersConfirmPassword("#KingSparkon1")
                        .build();

                var response =
                        restTemplate.exchange("/dev/api/auth/updatePassword", HttpMethod.PUT, sendRequestANdGetResponse(request), List.class);


                Assertions.assertEquals(HttpStatus.FORBIDDEN,response.getStatusCode());
            }
    }

}