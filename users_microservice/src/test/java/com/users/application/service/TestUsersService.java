package com.users.application.service;

import com.privileges.application.entity.Privileges;
import com.users.application.configurations.ApplicationConfig;
import com.users.application.configurations.JwtAuthFilterConfig;
import com.users.application.configurations.SecurityConfig;
import com.users.application.dtos.*;
import com.users.application.entities.Users;
import com.users.application.exceptions.UserNotFoundException;
import com.users.application.exceptions.UsersExistsException;
import com.users.application.mappers.UsersMapper;
import com.users.application.repository.UsersRepository;
import com.users.application.services.JwtService;
import com.users.application.services.UsersService;
import com.users.application.validators.UsersFieldsDataValidator;
import com.utils.application.RedisService;
import io.lettuce.core.AbstractRedisAsyncCommands;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;


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




    private String getMessageFromCause(String cause) {
        var index = cause.indexOf(":") + 2;

        System.out.print(cause.substring(index));
        return cause.substring(index);
    }

}

