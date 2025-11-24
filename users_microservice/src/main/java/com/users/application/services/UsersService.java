package com.users.application.services;


import com.users.application.exceptions.*;
import com.users.application.exceptions.controllerAdvice.UsersControllerAdvice;
import com.users.application.validators.UsersFieldsDataValidator;
import com.utils.application.RedisService;
import com.users.application.dtos.*;
import com.users.application.entities.Users;
import com.users.application.mappers.UsersMapper;
import com.users.application.repository.UsersRepository;
import com.utils.application.Execute;
import com.utils.application.globalExceptions.ServiceHandlerException;
import jakarta.transaction.Transactional;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.users.application.validators.UsersFieldsDataValidator.getInstance;
import static com.utils.application.ExceptionHandler.throwExceptionAndReport;
import static com.utils.application.HandlingLoadingService.handleServiceHandler;

@Service
public class UsersService implements Execute<List<UsersResponse>> {
    private static Logger logger = LoggerFactory.getLogger(UsersService.class);


    public static String serviceHandler;

    private UsersFullNameRequest usersFullNameRequest;
    @Setter
    private static UsersRepository usersRepository;
    private UsersRegisterRequest usersRegisterRequest;
    private UpdatePasswordRequest updatePasswordRequest;
    private LoginRequest loginRequest;
    private IdentityNoRequest identityNoRequest;
    private FindByIdRequest findByIdRequest;
    private static JwtService jwtService;
    private AuthenticationManager authenticationManager;
    @Setter
    private static UsersMapper usersMapper;
    private  static  PasswordEncoder passwordEncoder;
    private RedisService redisService;
    private UsersFieldsDataValidator validator;

    @Autowired
    public UsersService(PasswordEncoder passwordEncoder,@Autowired JwtService jwtService, @Autowired AuthenticationManager authenticationManager, @Autowired UsersRepository usersRepository, @Autowired UsersMapper usersMapper) {
        setUsersRepository(usersRepository);
        this.passwordEncoder = passwordEncoder;
        this.usersMapper = usersMapper;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public UsersFullNameRequest usersFullNameRequest() {
        return usersFullNameRequest;
    }

    public UsersService setUsersFullNameRequest(UsersFullNameRequest usersFullNameRequest) {
        this.usersFullNameRequest = usersFullNameRequest;
        return this;
    }

    public FindByIdRequest getFindByIdRequest() {
        return findByIdRequest;
    }

    public void setFindByIdRequest(FindByIdRequest findByIdRequest) {
        this.findByIdRequest = findByIdRequest;
    }

    public UsersRegisterRequest usersRegisterRequest() {
        return usersRegisterRequest;
    }

    public UsersService setUsersRegisterRequest(UsersRegisterRequest usersRegisterRequest) {
        this.usersRegisterRequest = usersRegisterRequest;
        return this;
    }

    public LoginRequest loginRequest() {
        return loginRequest;
    }

    public IdentityNoRequest identityNoRequest() {
        return identityNoRequest;
    }

    public UsersService setIdentityNoRequest(IdentityNoRequest identityNoRequest) {
        this.identityNoRequest = identityNoRequest;
        return this;
    }

    public UsersService setLoginRequest(LoginRequest loginRequest) {
        this.loginRequest = loginRequest;
        return this;
    }

    public UpdatePasswordRequest updatePasswordRequest() {
        return updatePasswordRequest;
    }

    public UsersService setUpdatePasswordRequest(UpdatePasswordRequest updatePasswordRequest) {
        this.updatePasswordRequest = updatePasswordRequest;
        return this;
    }


    public static void setServiceHandler(String serviceHandler) {
        UsersService.serviceHandler = serviceHandler;
    }

    @Transactional
    private List<UsersResponse> registerUsers() {

        var request = this.usersRegisterRequest();
        if (request == null) {
            throw new NullPointerException("Request sent by client is null");
        }

        Optional<Users> entitiesList;
        try {
            entitiesList = usersRepository.findByUserIdentityNo(getInstance().validateIdentityNo(request.getUserIdentityNo()));
        } catch (Exception e) {
            entitiesList   = null;
            e.printStackTrace();
        }

        if (entitiesList.isPresent()) {
            if (entitiesList.get().getUserStatus() == 0) {
                var errorMessage = "User has already been registered, email or cellphone number not verified";
                var resolveIssue = "click the verify or go to nearest AA registered company";
                throw throwExceptionAndReport(new UsersExistsException(errorMessage), errorMessage, resolveIssue);
            } else {
                var errorMessage = "User has already been registered";
                var resolveIssue = "Identity number already registered, please login";
                throw throwExceptionAndReport(new UsersExistsException(errorMessage), errorMessage, resolveIssue);
            }
        } else {


            Users users = Users.builder()
                    .userIdentityNo(getInstance().validateIdentityNo(request.getUserIdentityNo()))
                    .userPassword(passwordEncoder.encode(getInstance().checkPasswordValidity(usersRegisterRequest().getUserPassword())))
                    .userRegistrationDate(getInstance().formatDateTime(LocalDateTime.now()))
                    .userModifiedDate(getInstance().formatDateTime(LocalDateTime.now()))
                    .privileges(request.getPrivileges())
                    .userStatus((short) 0)
                    .userCellphoneNo(getInstance().validateCellphoneNo(request.getUserCellphoneNo()))
                    .userFullName(request.getUserFullName())
                    .userEmailAddress(request.getUserEmailAddress())
                    .userAge(getInstance().getValidatedAge())
                    .build();


            final var entity = usersRepository.save(users);
            var entityList = new ArrayList<Users>();
            entityList.add(users);
            var responseList = entityList
                    .stream()
                    .map(s -> UsersResponse
                            .builder()
                            .usersAge(users.getUserAge())
                            .id(users.getId())
                            .usersStatus(users.getUserStatus())
                            .usersEmailAddress(users.getUserEmailAddress())
                            .usersRegistrationDate(users.getUserRegistrationDate())
                            .usersModifiedDate(users.getUserModifiedDate())
                            .usersFullName(users.getUserFullName())
                            .usersIdentityNo(users.getUserIdentityNo())
                            .cellphoneNo(users.getUserCellphoneNo())
                            .privileges(users.getPrivileges())
                            .build())
                    .toList();
            logger.info("User : {} successfully registered data : {}", usersRegisterRequest().getUserFullName(), responseList);
            return responseList;
        }
    }

    private List<UsersResponse> findUserByUsersIdentityNo() {
        String encrypt = passwordEncoder.encode(identityNoRequest.usersIdentityNo());
        UsersResponse redisUserResponse = redisService.get(encrypt, UsersResponse.class);

        if (redisUserResponse != null) {
            logger.info("User with identity no : {} successfully found from cache, data is {}  ", redisUserResponse.getUsersIdentityNo(), redisUserResponse);
            return List.of(redisUserResponse);
        } else {
            var responseList = usersRepository.findByUserIdentityNo(identityNoRequest.usersIdentityNo()).stream().map(usersMapper::toDto).toList();

            if (responseList.size() == 1) {
                var jpaUserResponse = responseList.get(0);
                redisService.set(encrypt, jpaUserResponse, 6L, TimeUnit.HOURS);
                logger.info("User with identity no : {} successfully found from jpa, data is {}  ", redisUserResponse.getUsersIdentityNo(), jpaUserResponse);

                return responseList;
            } else {
                var errorMessage = "User with identity no ending with XXX-XXX-XXX-" + identityNoRequest.usersIdentityNo().substring(9) + "not found";
                var resolveIssue = "Please review the identity number inserted";
                throw throwExceptionAndReport(new UserNotFoundException(errorMessage), errorMessage, resolveIssue);
            }
        }

    }

    @Transactional
    private List<UsersResponse> updateUsersPassword() {


        var dbEntity = usersRepository.findByUserEmailAddress(this.updatePasswordRequest().getUsersEmailAddress());
        if (dbEntity.isPresent()) {
            var user = dbEntity.get();
            user.setUserPassword(this.updatePasswordRequest().getUsersPassword());
            if (this.updatePasswordRequest().getUsersPassword().equals(this.updatePasswordRequest().getUsersConfirmPassword())) {
                var updateEntity = usersRepository.save(user);
                logger.info("user with email address {} successfully updated password", updateEntity.getUserEmailAddress());
                return List.of(updateEntity).stream().map(usersMapper::toDto).toList();
            } else {
                var errorMessage = "new password and confirm password don't match";
                var resolveIssue = "Please confirm your new password and confirmation password";
                throw throwExceptionAndReport(new PasswordMisMatchException(errorMessage), errorMessage, resolveIssue);
            }
        } else {
            var errorMessage = "User doesn't exists, check your email address";
            var resolveIssue = "Please check your email address";
            throw throwExceptionAndReport(new UserEmailDoesNotExistException(errorMessage), errorMessage, resolveIssue);
        }
    }


    private List<UsersResponse> findUserById() {

        String encrypt = passwordEncoder.encode(this.getFindByIdRequest().getPkUsersId().toString());
        UsersResponse redisUserResponse = redisService.get(encrypt, UsersResponse.class);

        if (redisUserResponse != null) {
            logger.info("user with id {} successfully retrieved from cache data : {}", redisUserResponse.getId(), redisUserResponse);

            return List.of(redisUserResponse);
        } else {
            var responseList = usersRepository.findById(this.getFindByIdRequest().getPkUsersId()).stream().map(usersMapper::toDto).toList();

            if (responseList.size() == 1) {
                var jpaUserResponse = responseList.get(0);
                redisService.set(encrypt, jpaUserResponse, 6L, TimeUnit.HOURS);
                logger.info("user with id {} successfully retrieved from jpa data : {}", jpaUserResponse.getId(), jpaUserResponse);

                return responseList;
            } else {
                var errorMessage = "user id : " + this.getFindByIdRequest().getPkUsersId() + " not found";
                var resolveIssue = "Please review id inserted";
                throw throwExceptionAndReport(new UserNotFoundException(errorMessage), errorMessage, resolveIssue);
            }
        }
    }


    private List<UsersResponse> findAllUsers() {

        return usersRepository.findAll().stream().map(usersMapper::toDto).toList();
    }

    private List<UsersResponse> findAllUsersByName() {
        String encrypt = passwordEncoder.encode(usersFullNameRequest().usersFullName());
        UsersResponse redisUserResponse = redisService.get(encrypt, UsersResponse.class);

        if (redisUserResponse != null) {
            logger.info("user with name {} successfully retrieved from cache data : {}", redisUserResponse.getUsersFullName(), redisUserResponse);

            return List.of(redisUserResponse);
        } else {
            var responseList = usersRepository.findByUserFullName(usersFullNameRequest().usersFullName()).stream().map(usersMapper::toDto).toList();

            if (responseList.size() == 1) {
                var jpaUserResponse = responseList.get(0);
                redisService.set(encrypt, jpaUserResponse, 6L, TimeUnit.HOURS);
                logger.info("user with name {} successfully retrieved from jpa data : {}", jpaUserResponse.getUsersFullName(), jpaUserResponse);

                return responseList;
            } else {
                var errorMessage = "user with full name :" + usersFullNameRequest().usersFullName() + " not found";
                var resolveIssue = "Please review the full name inserted";
                throw throwExceptionAndReport(new UserNotFoundException(errorMessage), errorMessage, resolveIssue);
            }
        }

    }

    private boolean redisStatus, passwordStatus;


    @Transactional
    private List<UsersResponse> login() {
        var encrypt = passwordEncoder.encode(loginRequest().getUsersEmailAddress());
        UsersResponse redisUserResponse = redisService.get(encrypt, UsersResponse.class);
        UsersResponse jpaUserResponse;
        if (redisUserResponse != null) {
            System.out.println("redis");
            redisStatus = true;
            jpaUserResponse = null;

        } else {
            System.out.println("Jpa");
            redisStatus = false;
            var entity = usersMapper.toEntity(loginRequest());
            jwtService.generateToken(entity);

            var responseList = usersRepository.findByUserEmailAddress(entity.getUserEmailAddress()).stream().map(usersMapper::toDto).toList();


            if (responseList.size() == 1) {
                passwordStatus = true;
                jpaUserResponse = responseList.get(0);
                redisService.set(encrypt, jpaUserResponse, 6L, TimeUnit.HOURS);
            } else {
                jpaUserResponse = null;
                passwordStatus = false;
            }

        }

        try {
            if (redisStatus) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest().getUsersEmailAddress(), loginRequest().getUsersPassword()));
                logger.info("user with email {} successfully logged in using cache data : {}", redisUserResponse.getUsersEmailAddress(), redisUserResponse);

                return List.of(redisUserResponse);
            } else {
                logger.info("user with email {} successfully logged in using jpa data : {}", loginRequest().getUsersEmailAddress(), jpaUserResponse);

                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest().getUsersEmailAddress(), loginRequest().getUsersPassword()));
                return List.of(jpaUserResponse);
            }
        } catch (AuthenticationException e) {
            if (redisStatus) {
                var errorMessage = "cached data shows change of password ";
                var resolveIssue = "please log in again";
                throw throwExceptionAndReport(new CachedUsersPasswordChangedException(errorMessage), errorMessage, resolveIssue);
            } else {
                if (passwordStatus) {
                    var errorMessage = UsersControllerAdvice.setMessage("password inserted is incorrect");
                    var resolveIssue = "please provide correct password or update password";
                    throw throwExceptionAndReport(new UsersPasswordIncorrectException(errorMessage), errorMessage, resolveIssue);
                } else {
                    var errorMessage = UsersControllerAdvice.setMessage("email address" + loginRequest().getUsersEmailAddress() + " is not found");
                    var resolveIssue = "please provide correct password or register with the email address";
                    throw throwExceptionAndReport(new UserNotFoundException(errorMessage), errorMessage, resolveIssue);
                }
            }

        }
    }

    @Override
    public List<UsersResponse> call() {
        if (handleServiceHandler(serviceHandler) != "START_SERVICE")
            switch (serviceHandler) {
                case "registerUsers":
                    return this.registerUsers();
                case "getAllUsers":
                    return this.findAllUsers();
                case "getUsersByFullName":
                    return this.findAllUsersByName();
                case "getUsersByIdentityNo":
                    return this.findUserByUsersIdentityNo();
                case "getUsersById":
                    return this.findUserById();
                case "userLogin":
                    return this.login();
                default:
                    throw new ServiceHandlerException("Failed execute service due to incorrect service string");
            }
        else
            return new ArrayList<>();
    }

    @Override
    public void setCache(@Autowired RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void setEncodeCacheKey(@Autowired PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
