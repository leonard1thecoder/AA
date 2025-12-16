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
import lombok.Getter;
import lombok.Setter;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

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
    private static LoginRequest loginRequest;
    private static IdentityNoRequest identityNoRequest;
    @Getter
    private FindByIdRequest findByIdRequest;
    private static JwtService jwtService;

    private static AuthenticationManager authenticationManager;
    @Setter
    private static UsersMapper usersMapper;
    private static PasswordEncoder passwordEncoder;
    private static RedisService redisService;
    private UsersFieldsDataValidator validator;

    @Autowired
    public UsersService(@Autowired RedisTemplate redisTemplate, PasswordEncoder passwordEncoder, @Autowired JwtService jwtService, @Autowired AuthenticationManager authenticationManager, @Autowired UsersRepository usersRepository, @Autowired UsersMapper usersMapper) {
        setUsersRepository(usersRepository);

        UsersService.passwordEncoder = passwordEncoder;
        UsersService.usersMapper = usersMapper;
        UsersService.jwtService = jwtService;
        UsersService.redisService = new RedisService(redisTemplate);
        UsersService.authenticationManager = authenticationManager;
    }

    public UsersFullNameRequest usersFullNameRequest() {
        return usersFullNameRequest;
    }

    public UsersService setUsersFullNameRequest(UsersFullNameRequest usersFullNameRequest) {
        this.usersFullNameRequest = usersFullNameRequest;
        return this;
    }

    public void setFindByIdRequest(FindByIdRequest findByIdRequest) {
        this.findByIdRequest = findByIdRequest;
    }

    public UsersRegisterRequest usersRegisterRequest() {
        return usersRegisterRequest;
    }

    public void setUsersRegisterRequest(UsersRegisterRequest usersRegisterRequest) {
        this.usersRegisterRequest = usersRegisterRequest;
    }

    public LoginRequest loginRequest() {
        return loginRequest;
    }

    public IdentityNoRequest identityNoRequest() {
        return identityNoRequest;
    }

    public void setIdentityNoRequest(IdentityNoRequest identityNoRequest) {
        this.identityNoRequest = identityNoRequest;
    }

    public void setLoginRequest(LoginRequest loginRequest) {
        this.loginRequest = loginRequest;
    }

    public UpdatePasswordRequest updatePasswordRequest() {
        return updatePasswordRequest;
    }

    public void setUpdatePasswordRequest(UpdatePasswordRequest updatePasswordRequest) {
        this.updatePasswordRequest = updatePasswordRequest;
    }


    public static void setServiceHandler(String serviceHandler) {
        UsersService.serviceHandler = serviceHandler;
    }

    /*
        Need to implement redis cache
     */
    @Transactional
    private List<UsersResponse> registerUsers() {

        var request = this.usersRegisterRequest();
        if (request == null) {
            var errorMessage = "Users registration request is  null";
            var resolveIssue = "Some of your data is registered, contact AA for verification";
            throw throwExceptionAndReport(new NullRequestException(errorMessage), errorMessage, resolveIssue);        }else {

            Optional<Users> entitiesList;

            entitiesList = usersRepository.findByUserCellphoneNo(getInstance().validateCellphoneNo(request.getUserCellphoneNo()));

            logger.info("entities list : {} ",entitiesList );
            if (entitiesList.isPresent()) {
                if (entitiesList.get().getUserStatus() == 0) {
                    var errorMessage = "User has already been registered, email or cellphone number not verified";
                    var resolveIssue = "Login to get verification link";
                    throw throwExceptionAndReport(new UserNotVerifiedException(errorMessage), errorMessage, resolveIssue);
                } else {
                    var errorMessage = "User has already been registered";
                    var resolveIssue = "Please login";
                    throw throwExceptionAndReport(new UsersExistsException(errorMessage), errorMessage, resolveIssue);
                }
            } else {

                if(request.getPrivileges() > 4 || request.getPrivileges() <1){
                    var errorMessage = "AA agent do not have privilege id of : " + request.getPrivileges();
                    var resolveIssue = "use registration form to register in official website or app";
                    throw throwExceptionAndReport(new PrivilegeIdOutOfBoundException(errorMessage), errorMessage, resolveIssue);
                }

                Users users = Users.builder()
                        .userIdentityNo(getInstance().validateIdentityNo(request.getUserIdentityNo()))
                        .userPassword(passwordEncoder.encode(getInstance().checkPasswordValidity(usersRegisterRequest().getUserPassword().trim())))
                        .userRegistrationDate(getInstance().formatDateTime(LocalDateTime.now()))
                        .userModifiedDate(getInstance().formatDateTime(LocalDateTime.now()))
                        .fk_privilege_id(request.getPrivileges())
                        .userStatus((short) 0)
                        .userCellphoneNo(getInstance().validateCellphoneNo(request.getUserCellphoneNo().trim()))
                        .userFullName(request.getUserFullName().trim())
                        .userEmailAddress(request.getUserEmailAddress().trim())
                        .userAge(getInstance().getValidatedAge())
                        .build();

                try {
                    logger.info("users was successfully registered : data : {}", usersRepository.save(users));
                    var entityList = new ArrayList<Users>();
                    entityList.add(users);
                    var responseList = entityList
                            .stream()
                            .map(s -> UsersResponse
                                    .builder()
                                    .usersAge(s.getUserAge())
                                    .id(s.getId())
                                    .usersStatus(s.getUserStatus())
                                    .usersEmailAddress(s.getUserEmailAddress())
                                    .usersRegistrationDate(s.getUserRegistrationDate())
                                    .usersModifiedDate(s.getUserModifiedDate())
                                    .usersFullName(s.getUserFullName())
                                    .usersIdentityNo(s.getUserIdentityNo())
                                    .cellphoneNo(s.getUserCellphoneNo())
                                    .privileges(s.getFk_privilege_id())
                                    .build())
                            .toList();

                    if(redisService.get("ALL_USERS",UsersResponse.class) != null) {
                        logger.info("Cached data for all users deleted  : {}", redisService.delete("ALL_USERS"));
                    }

                    logger.info("User : {} successfully registered data : {}", usersRegisterRequest().getUserFullName(), responseList);
                    return responseList;

                } catch (DataIntegrityViolationException e) {
                    var errorMessage = "User has already been registered";
                    var resolveIssue = "Some of your data is registered, contact AA for verification";
                    throw throwExceptionAndReport(new UsersExistsException(errorMessage), errorMessage, resolveIssue);
                }
            }
        }
    }



    /*
          this code is out of scope v1
     */
//    private List<UsersResponse> findUserByUsersIdentityNo() {
//        String encrypt = getInstance().validateIdentityNo(identityNoRequest.getUsersIdentityNo());
//        UsersResponse redisUserResponse = redisService.get(encrypt, UsersResponse.class);
//
//        if (redisUserResponse != null) {
//            logger.info("User with identity no : {} successfully found from cache, data is {}  ", redisUserResponse.getUsersIdentityNo(), redisUserResponse);
//            return List.of(redisUserResponse);
//        } else {
//            var responseList = usersRepository.findByUserIdentityNo(identityNoRequest.getUsersIdentityNo()).stream().map(usersMapper::toDto).toList();
//
//            if (responseList.size() == 1) {
//                var jpaUserResponse = responseList.get(0);
//                redisService.set(encrypt, jpaUserResponse, 6L, TimeUnit.HOURS);
//                logger.info("User with identity no : {} successfully found from jpa, data is {}  ", redisUserResponse.getUsersIdentityNo(), jpaUserResponse);
//
//                return responseList;
//            } else {
//                var errorMessage = "User with identity no ending with XXX-XXX-XXX-" + identityNoRequest.getUsersIdentityNo().substring(9) + "not found";
//                var resolveIssue = "Please review the identity number inserted";
//                throw throwExceptionAndReport(new UserNotFoundException(errorMessage), errorMessage, resolveIssue);
//            }
//        }
//
//    }

    @Transactional
    private List<UsersResponse> updateUsersPassword() {


        try {
            var dbEntity = usersRepository.findByUserEmailAddress(this.updatePasswordRequest().getUsersEmailAddress());
            if (dbEntity.isPresent()) {
                var user = dbEntity.get();
                if (getInstance().checkPasswordValidity(this.updatePasswordRequest().getUsersPassword()).equals(getInstance().checkPasswordValidity(this.updatePasswordRequest().getUsersConfirmPassword()))) {
                    user.setUserPassword(passwordEncoder.encode(this.updatePasswordRequest().getUsersPassword()));
                    user.setUserModifiedDate(getInstance().formatDateTime(LocalDateTime.now()));
                    var updateEntity = usersRepository.save(user);

                    if(redisService.get(updateEntity.getUserEmailAddress(),UsersResponse.class) != null) {
                        logger.info("user with email address {} successfully successfully removed from cache", redisService.delete(updateEntity.getUserEmailAddress()));
                    }
                    logger.info("user with email address {} successfully updated password", updateEntity.getUserEmailAddress());
                    return Stream.of(updateEntity).map(s -> UsersResponse
                            .builder()
                            .usersAge(s.getUserAge())
                            .id(s.getId())
                            .usersStatus(s.getUserStatus())
                            .usersEmailAddress(s.getUserEmailAddress())
                            .usersRegistrationDate(s.getUserRegistrationDate())
                            .usersModifiedDate(s.getUserModifiedDate())
                            .usersFullName(s.getUserFullName())
                            .usersIdentityNo(s.getUserIdentityNo())
                            .cellphoneNo(s.getUserCellphoneNo())
                            .privileges(s.getFk_privilege_id())
                            .build()).toList();
                } else {
                    var errorMessage = "new password and confirm password don't match";
                    var resolveIssue = "Please confirm your new password and confirmation password";
                    throw throwExceptionAndReport(new PasswordMisMatchException(errorMessage), errorMessage, resolveIssue);
                }
            } else {
                var errorMessage = "User doesn't exists, check your email address";
                var resolveIssue = "Please check your email address, re-enter email address";
                throw throwExceptionAndReport(new UserEmailDoesNotExistException(errorMessage), errorMessage, resolveIssue);
            }
        } catch (NullPointerException e) {
            var errorMessage = "Update password request is null";
            var resolveIssue = "contact AA administrator";
            throw throwExceptionAndReport(new NullRequestException(errorMessage), errorMessage, resolveIssue);
        }
    }

    private List<UsersResponse> findUserById() {
        String encrypt;
        try {
            encrypt = this.getFindByIdRequest().getId().toString();

            UsersResponse redisUserResponse = redisService.get(encrypt, UsersResponse.class);

            if (redisUserResponse != null) {
                logger.info("user with id {} successfully retrieved from cache data : {}", redisUserResponse.getId(), redisUserResponse);

                return List.of(redisUserResponse);
            } else {
                var responseList = usersRepository.findById(this.getFindByIdRequest().getId());

                if (responseList.isPresent()) {
                    var users = responseList.get();
                    List<Users> list = new ArrayList<>();
                    list.add(users);
                    var jpaUserResponse = list.stream().map(s -> UsersResponse
                            .builder()
                            .usersAge(s.getUserAge())
                            .id(s.getId())
                            .usersStatus(s.getUserStatus())
                            .usersEmailAddress(s.getUserEmailAddress())
                            .usersRegistrationDate(s.getUserRegistrationDate())
                            .usersModifiedDate(s.getUserModifiedDate())
                            .usersFullName(s.getUserFullName())
                            .usersIdentityNo(s.getUserIdentityNo())
                            .cellphoneNo(s.getUserCellphoneNo())
                            .privileges(s.getFk_privilege_id())
                            .build()).toList();

                    redisService.set(encrypt, jpaUserResponse.get(0), 6L, TimeUnit.HOURS);
                    logger.info("cached data : {}", redisService.get(encrypt, UsersResponse.class));
                    logger.info("user with id {} successfully retrieved from jpa data : {}", jpaUserResponse.get(0).getId(), jpaUserResponse);
                    return jpaUserResponse;
                } else {
                    var errorMessage = "user id : " + this.getFindByIdRequest().getId() + " not found";
                    var resolveIssue = "Please review id inserted";
                    throw throwExceptionAndReport(new UserNotFoundException(errorMessage), errorMessage, resolveIssue);
                }
            }
        } catch (NullPointerException e) {
            var errorMessage = "Find by id request is null ";
            var resolveIssue = "Contact AA System administrator";
            throw throwExceptionAndReport(new RuntimeException(errorMessage), errorMessage, resolveIssue);
        }

    }


    private List<UsersResponse> findAllUsers() {

        return usersRepository.findAll().stream().map(s -> UsersResponse
                .builder()
                .usersAge(s.getUserAge())
                .id(s.getId())
                .usersStatus(s.getUserStatus())
                .usersEmailAddress(s.getUserEmailAddress())
                .usersRegistrationDate(s.getUserRegistrationDate())
                .usersModifiedDate(s.getUserModifiedDate())
                .usersFullName(s.getUserFullName())
                .usersIdentityNo(s.getUserIdentityNo())
                .cellphoneNo(s.getUserCellphoneNo())
                .privileges(s.getFk_privilege_id())
                .build()).toList();

    }

    private List<UsersResponse> findAllUsersByName() {

        try {
            String encrypt = usersFullNameRequest().getUsersFullName();
            UsersResponse redisUserResponse = redisService.get(encrypt, UsersResponse.class);
            if (redisUserResponse != null) {
                logger.info("user with name {} successfully retrieved from cache data : {}", redisUserResponse.getUsersFullName(), redisUserResponse);

                return List.of(redisUserResponse);
            } else {
                var responseList = usersRepository.findByUserFullName(usersFullNameRequest().getUsersFullName());

                if (responseList.isPresent()) {
                    var users = responseList.get();
                    List<Users> list = new ArrayList<>();
                    list.add(users);
                    var jpaUserResponse = list.stream().map(s -> UsersResponse
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
                            .privileges(users.getFk_privilege_id())
                            .build()).toList();
                    redisService.set(encrypt, jpaUserResponse.get(0), 6L, TimeUnit.HOURS);
                    logger.info("user with name {} successfully retrieved from jpa data : {}", jpaUserResponse.get(0).getUsersFullName(), jpaUserResponse);

                    return jpaUserResponse;
                } else {
                    var errorMessage = "user with full name :" + usersFullNameRequest().getUsersFullName() + " not found";
                    var resolveIssue = "Please review the full name inserted";
                    throw throwExceptionAndReport(new UserNotFoundException(errorMessage), errorMessage, resolveIssue);
                }
            }

        } catch (NullPointerException e) {
            var errorMessage = "Full name request is null";
            var resolveIssue = "Contact AA Administrator";
            throw throwExceptionAndReport(new RuntimeException(errorMessage), errorMessage, resolveIssue);
        }


    }

    private boolean passwordStatus;


    @Transactional
    private List<UsersResponse> login() {

        try {
            var encrypt = loginRequest().getUsersEmailAddress();
            UsersResponse redisUserResponse = redisService.get(encrypt, UsersResponse.class);
            UsersResponse jpaUserResponse;
            boolean redisStatus;
            if (redisUserResponse != null) {
                System.out.println("redis");
                redisStatus = true;
                jpaUserResponse = null;

            } else {
                System.out.println("Jpa");
                redisStatus = false;


                var optionalEntity = usersRepository.findByUserEmailAddress(loginRequest().getUsersEmailAddress());


                if (optionalEntity.isPresent()) {
                    logger.info("email address {} found",loginRequest().getUsersEmailAddress());
                    var users = optionalEntity.get();
                    List<Users> list = new ArrayList<>();
                    list.add(users);
                    jpaUserResponse = list.stream().map(s -> UsersResponse
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
                                    .privileges(users.getFk_privilege_id())
                                    .build()).toList()
                            .get(0);

                    passwordStatus = true;
                    jwtService.generateToken(optionalEntity.get());


                } else {
                    logger.info("email address {} not found",loginRequest().getUsersEmailAddress());
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

                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest().getUsersEmailAddress(), loginRequest().getUsersPassword()));
                    logger.info("user with email {} successfully logged in using jpa data : {}", loginRequest().getUsersEmailAddress(), jpaUserResponse);
                    redisService.set(encrypt, jpaUserResponse, 6L, TimeUnit.HOURS);

                    logger.info("cached login data : {}", redisService.get(encrypt, UsersResponse.class));

                    return List.of(jpaUserResponse);
                }
            } catch (AuthenticationException e) {
                if (redisStatus) {

                    logger.info("delete cached data for login : {}",redisService.delete(encrypt));
                    var errorMessage = "cached data shows change of password ";
                    var resolveIssue = "please log in again";
                    throw throwExceptionAndReport(new CachedUsersPasswordChangedException(errorMessage), errorMessage, resolveIssue);
                } else {
                    if (passwordStatus) {
                        var errorMessage = UsersControllerAdvice.setMessage("password inserted is incorrect");
                        var resolveIssue = "please provide correct password or update password";
                        throw throwExceptionAndReport(new UsersPasswordIncorrectException(errorMessage), errorMessage, resolveIssue);
                    } else {
                        var errorMessage = UsersControllerAdvice.setMessage("email address " + loginRequest().getUsersEmailAddress() + " not found, verify your email or register");
                        var resolveIssue = "Enter correct email address or register using the email entered";
                        throw throwExceptionAndReport(new UserNotFoundException(errorMessage), errorMessage, resolveIssue);
                    }
                }

            }

        } catch (NullPointerException e) {
            var errorMessage = UsersControllerAdvice.setMessage("Login request is null");
            var resolveIssue = "Contact AA Administrator";
            throw throwExceptionAndReport(new UserNotFoundException(errorMessage), errorMessage, resolveIssue);
        }

    }

    @Override
    public List<UsersResponse> call() {
        if (!handleServiceHandler(serviceHandler).equals("START_SERVICE"))
            return switch (serviceHandler) {
                case "registerUsers" -> this.registerUsers();
                case "getAllUsers" -> this.findAllUsers();
                case "getUsersByFullName" -> this.findAllUsersByName();
                //code is out of scope v1
//                case "getUsersByIdentityNo":
//                    return this.findUserByUsersIdentityNo();
                case "getUsersById" -> this.findUserById();
                case "userLogin" -> this.login();
                case "userUpdatePassword" -> this.updateUsersPassword();

                default -> throw new ServiceHandlerException("Failed execute service due to incorrect service string");
            };
        else
            return null;
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
