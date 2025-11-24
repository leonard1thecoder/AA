package com.users.application.service;

import com.privileges.application.entity.Privileges;
import com.users.application.configurations.ApplicationConfig;
import com.users.application.configurations.JwtAuthFilterConfig;
import com.users.application.configurations.SecurityConfig;
import com.users.application.dtos.UsersRegisterRequest;
import com.users.application.entities.Users;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;



@SpringBootTest
public class TestUsersService {


    private UsersMapper mapper;

    @Nested
    class TestSignUp {


        @Mock
        private UsersRepository rep;
        private UsersRegisterRequest request;

        @Autowired
        private UsersService service;

        @Mock
        private UsersMapper mapper;

        @BeforeEach
        public void initRegisterRequest(){
            
           request = UsersRegisterRequest.builder()
                   .privileges(new Privileges(1,"users",(byte)1))
                   .userCellphoneNo("0788924833")
                   .userEmailAddress("emai811l@email.com")
                   .userFullName("Sizolwakhe Leonard Mthimunye")
                   .userIdentityNo("9711229953088")
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
                NullPointerException throwable = Assertions.assertThrows(NullPointerException.class,()->{
                    throw new NullPointerException("Request sent by client is null");
                });

                        Assertions.assertEquals(throwable.getMessage(),getMessageFromCause(e.getCause().toString()));
            }
        }


        @Test
        void testRegisterUsersMethod_requestNotNull() throws NoSuchMethodException, IllegalAccessException {
            //when
            service.setUsersRegisterRequest(request);

            //Given
            Method registerUsersMethod = UsersService.class.getDeclaredMethod("registerUsers");
            registerUsersMethod.setAccessible(true);
            try {
                registerUsersMethod.invoke(service);
            } catch (InvocationTargetException e) {
                NullPointerException throwable = Assertions.assertThrows(NullPointerException.class,()->{
                    throw new NullPointerException("Mapper class is not instantiated");
                });

                Assertions.assertEquals(throwable.getMessage(),getMessageFromCause(e.getCause().toString()));
            }
        }

        @Test
        void testRegisterUsersMethod_userAlreadyRegistered() throws NoSuchMethodException, IllegalAccessException {
            //when
            var map = mapper.toEntity(request);
//            repository.save(map);


            //Given
            Method registerUsersMethod = UsersService.class.getDeclaredMethod("registerUsers");
            registerUsersMethod.setAccessible(true);
            try {
                registerUsersMethod.invoke(service);
            } catch (InvocationTargetException e) {
                NullPointerException throwable = Assertions.assertThrows(NullPointerException.class,()->{
                    throw new UsersExistsException("User has already been registered");
                });

                Assertions.assertEquals(throwable.getMessage(),getMessageFromCause(e.getCause().toString()));
            }
        }
    }


    private String getMessageFromCause(String cause){
        var index = cause.indexOf(":") + 2;

        System.out.print(cause.substring(index));
        return cause.substring(index);
    }

}

