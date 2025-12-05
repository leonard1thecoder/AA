package com.users.application.service;

import com.privileges.application.entity.Privileges;
import com.users.application.dtos.*;
import com.users.application.exceptions.PasswordMisMatchException;
import com.users.application.exceptions.UserEmailDoesNotExistException;
import com.users.application.exceptions.UserNotFoundException;
import com.users.application.mappers.UsersMapper;
import com.users.application.repository.UsersRepository;
import com.users.application.services.UsersService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


@SpringBootTest
public class TestUsersService {


    private UsersMapper mapper;
    @Autowired
    private UsersService service;
    @Nested
    class TestSignUp {


        @Autowired
        private UsersRepository rep;
        private UsersRegisterRequest request;



        @Mock
        private UsersMapper mapper;

        @BeforeEach
        public void initRegisterRequest() {

            request = UsersRegisterRequest.builder()
                    .privileges(new Privileges(1, "users", (byte) 1))
                    .userCellphoneNo("0891231230")
                    .userEmailAddress("em2ail2@email.com")
                    .userFullName("Sizolwakhe Leonard Mthimunye")
                    .userIdentityNo("9708316953188")
                    .userPassword("#AA@mail1.com")
                    .userStatus((short) 1)
                    .build();
        }


        @Test
        void testRegisterUsersMethod_nullRequest() throws NoSuchMethodException, IllegalAccessException {
            //when
            request = null;
            service.setUsersRegisterRequest(request);

            //Given
            Method registerUsersMethod = UsersService.class.getDeclaredMethod("registerUsers");
            registerUsersMethod.setAccessible(true);
            try {
                registerUsersMethod.invoke(service);
            } catch (InvocationTargetException e) {
                NullPointerException throwable = Assertions.assertThrows(NullPointerException.class, () -> {
                    throw new NullPointerException("Request sent by client is null");
                });

                Assertions.assertEquals(throwable.getMessage(), getMessageFromCause(e.getCause().toString()));
            }
        }


        @Test
        void testRegisterUsersMethod_requestNotNull() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
            //when
            service.setUsersRegisterRequest(request);

            //Given
            Method registerUsersMethod = UsersService.class.getDeclaredMethod("registerUsers");
            registerUsersMethod.setAccessible(true);
            registerUsersMethod.invoke(service);


        }



        @Test
        void testRegisterUsersMethod_userSignUp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
            service.setUsersRegisterRequest(request);

            //Given
            Method registerUsersMethod = UsersService.class.getDeclaredMethod("registerUsers");
            registerUsersMethod.setAccessible(true);

            var response = (List<UsersResponse>) registerUsersMethod.invoke(service);
            Assertions.assertEquals(1, response.size());

        }

    }

    @Nested
    class TestFindById {

        @Test
        void testFindUserByUsersId_findUsersByNonExistingIdInDb() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            //when
            var request = FindByIdRequest.builder()
                    .id(97L)
                    .build();
            service.setFindByIdRequest(request);

            //Given
            Method findUserByIdMethod = UsersService.class.getDeclaredMethod("findUserById");
            findUserByIdMethod.setAccessible(true);
            try {
                findUserByIdMethod.invoke(service);
            } catch (InvocationTargetException e) {
                UserNotFoundException throwable = Assertions.assertThrows(UserNotFoundException.class, () -> {
                    throw new UserNotFoundException("user id : " + request.getId() + " not found");
                });

                Assertions.assertEquals(throwable.getMessage(), getMessageFromCause(e.getCause().toString()));
            }
        }

        @Test
        void testFindUserByUsersId_findUsersByExistingIdInDb() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            //when
            var request = FindByIdRequest.builder()
                    .id(52L)
                    .build();
            service.setFindByIdRequest(request);

            //Given
            Method findUserByIdMethod = UsersService.class.getDeclaredMethod("findUserById");
            findUserByIdMethod.setAccessible(true);

     var list = (List<UsersResponse>)findUserByIdMethod.invoke(service);


                Assertions.assertEquals(1, list.size());

        }


    }

    @Nested
    class TestLogin{
        @Test
        void  testLoginMethod_successfulLogin() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
            //when
            LoginRequest request = LoginRequest.builder()
                    .usersEmailAddress("em2ail2@email.com")
                    .usersPassword("#AA@mail1.com")
                    .build();
            service.setLoginRequest(request);

            //Given

            Method loginMethod = UsersService.class.getDeclaredMethod("login");
            loginMethod.setAccessible(true);

            var list = (List<UsersResponse>)loginMethod.invoke(service);

            //Assert
            Assertions.assertEquals(1, list.size());
        }

        @Test
        void  testLoginMethod_unsuccessfulLoginIncorrectPassword() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
            //when
            LoginRequest request = LoginRequest.builder()
                    .usersEmailAddress("em2ail2@email.com")
                    .usersPassword("#AA2@mail1.com")
                    .build();
            service.setLoginRequest(request);

            //Given

            Method loginMethod = UsersService.class.getDeclaredMethod("login");
            loginMethod.setAccessible(true);


            try {
                loginMethod.invoke(service);
            } catch (InvocationTargetException e) {
                UserNotFoundException throwable = Assertions.assertThrows(UserNotFoundException.class, () -> {
                    throw new UserNotFoundException("password inserted is incorrect");
                });

                Assertions.assertEquals(throwable.getMessage(), getMessageFromCause(e.getCause().toString()));
            }


        }

        @Test
        void  testLoginMethod_unsuccessfulLoginIncorrectEmailAddress() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
            //when
            LoginRequest request = LoginRequest.builder()
                    .usersEmailAddress("em222ail2@email.com")
                    .usersPassword("#AA2@mail1.com")
                    .build();
            service.setLoginRequest(request);

            //Given

            Method loginMethod = UsersService.class.getDeclaredMethod("login");
            loginMethod.setAccessible(true);


            try {
                loginMethod.invoke(service);
            } catch (InvocationTargetException e) {
                UserNotFoundException throwable = Assertions.assertThrows(UserNotFoundException.class, () -> {
                    throw new UserNotFoundException("email address " + request.getUsersEmailAddress() + " not found, verify your email or register");
                });

                Assertions.assertEquals(throwable.getMessage(), getMessageFromCause(e.getCause().toString()));
            }


        }
    }

    @Nested
    class FindByName{

        void testFindAllUsersByNameMethod_validFullNameFind() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            UsersFullNameRequest request = UsersFullNameRequest.builder()
                    .usersFullName("Sizolwakhe Leonard Mthimunye")
                    .build() ;
            service.setUsersFullNameRequest(request);


            //Given

            Method findAllUsersByNameMethod = UsersService.class.getDeclaredMethod("findAllUsersByName");
            findAllUsersByNameMethod.setAccessible(true);

            var list = (List<UsersResponse>)findAllUsersByNameMethod.invoke(service);

            //Assert
            Assertions.assertEquals(1, list.size());

        }
    }



    @Nested
    class TestUpdateUserPassword{


        @Test
        void testUpdateUsersPasswordMethod_validEmailAddressAndNewPasswordMatchConfirmPassword() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            //Given
            UpdatePasswordRequest request = UpdatePasswordRequest
                    .builder()
                    .usersEmailAddress("em2ail2@email.com")
                    .usersPassword("12345")
                    .usersConfirmPassword("12345")
                    .build();
            //When
           var usersPasswordMethod = UsersService.class.getDeclaredMethod("updateUsersPassword");
            usersPasswordMethod.setAccessible(true);
           var response = (List<UsersResponse>)usersPasswordMethod.invoke(service);

           //Assert
            Assertions.assertEquals(1, response.size());
        }

        @Test
        void testUpdateUsersPasswordMethod_validEmailAddressAndNewPasswordDoesNotMatchConfirmPassword() throws NoSuchMethodException,IllegalAccessException {
            //Given
            UpdatePasswordRequest request = UpdatePasswordRequest
                    .builder()
                    .usersEmailAddress("em2ail2@email.com")
                    .usersPassword("12345")
                    .usersConfirmPassword("1232")
                    .build();

            // When
            var usersPasswordMethod = UsersService.class.getDeclaredMethod("updateUsersPassword");
            usersPasswordMethod.setAccessible(true);

            try {
                usersPasswordMethod.invoke(service);
            }catch (InvocationTargetException e) {
                PasswordMisMatchException throwable = Assertions.assertThrows(PasswordMisMatchException.class, () -> {
                    throw new PasswordMisMatchException("new password and confirm password don't match");
                });

                Assertions.assertEquals(throwable.getMessage(), getMessageFromCause(e.getCause().toString()));
            }


        }

        @Test
        void testUpdateUsersPasswordMethod_invalidEmailAddress() throws NoSuchMethodException,IllegalAccessException {
            //Given
            UpdatePasswordRequest request = UpdatePasswordRequest
                    .builder()
                    .usersEmailAddress("em2ail22@email.com")
                    .build();

            // When
            var usersPasswordMethod = UsersService.class.getDeclaredMethod("updateUsersPassword");
            usersPasswordMethod.setAccessible(true);

            try {
                usersPasswordMethod.invoke(service);
            }catch (InvocationTargetException e) {
                UserEmailDoesNotExistException throwable = Assertions.assertThrows(UserEmailDoesNotExistException.class, () -> {
                    throw new UserEmailDoesNotExistException("User doesn't exists, check your email address");
                });

                Assertions.assertEquals(throwable.getMessage(), getMessageFromCause(e.getCause().toString()));
            }
        }
    }



    private String getMessageFromCause(String cause) {
        var index = cause.indexOf(":") + 2;

        System.out.print(cause.substring(index));
        return cause.substring(index);
    }

}

