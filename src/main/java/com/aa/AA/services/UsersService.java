package com.aa.AA.services;

import com.aa.AA.dtos.*;
import com.aa.AA.entities.UsersEntity;
import com.aa.AA.utils.exceptions.*;
import com.aa.AA.utils.exceptions.controllerAdvices.UsersControllerAdvice;
import com.aa.AA.utils.executors.Execute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.aa.AA.utils.mappers.UsersMapper;
import com.aa.AA.utils.repository.UsersRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.aa.AA.utils.HandlingLoadingService.*;
import static com.aa.AA.utils.exceptions.exceptionHandler.ExceptionHandler.throwExceptionAndReport;

@Service
public class UsersService implements Execute<List<UsersResponse>> {
    private Logger logger = LoggerFactory.getLogger(UsersService.class);


    public static String serviceHandler;

    private UsersFullNameRequest usersFullNameRequest;
    private UsersRepository usersRepository;
    private UsersRegisterRequest usersRegisterRequest;
    private UpdatePasswordRequest updatePasswordRequest;
    private LoginRequest loginRequest;
    private IdentityNoRequest identityNoRequest;
    private FindByIdRequest findByIdRequest;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private UsersMapper usersMapper;
    private PasswordEncoder passwordEncoder;
    private RedisService redisService;

    @Autowired
    public UsersService(@Autowired JwtService jwtService, @Autowired AuthenticationManager authenticationManager, @Autowired UsersRepository usersRepository, @Autowired UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
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

    private List<UsersResponse> registerUsers() {

        var request = this.usersRegisterRequest();


        var usersEntity = new UsersEntity(null, request.getFkPrivilegeId(), request.getUsersIdentityNo(), request.getNoPromotionToken(), request.getUsersStatus(), request.getUsersAge(), request.getUsersFullName(), request.getUsersEmailAddress(), request.getUsersPassword(), LocalDateTime.now().toString(), "", "");
        var entitiesList = usersRepository.findByUsersIdentityNo(request.getUsersIdentityNo());
        if (entitiesList.isPresent()) {

            if (request.getUsersAge() < 18) {
                var errorMessage = "Users under age 18 can't register";
               var resolveIssue = "Please ensure you inserting correct identity number";
                throw throwExceptionAndReport(new UserUnderAgeOf18Exception(errorMessage),errorMessage,resolveIssue);
            } else {
                var errorMessage = "User has already been registered";
                var resolveIssue ="Identity number already registered, please login";
                throw throwExceptionAndReport(new UsersExistsException(errorMessage),errorMessage,resolveIssue);
            }

        } else {
            usersEntity.setUsersPassword(passwordEncoder.encode(usersRegisterRequest().getUsersPassword()));
            var entity = usersRepository.save(usersEntity);
            entity.setToken(jwtService.generateToken(entity));
            var entityWithToken = usersRepository.save(entity);
            var entityList = new ArrayList<UsersEntity>();
            entityList.add(entityWithToken);
            var responseList = entityList.stream().map(usersMapper::toDto).toList();
            logger.info("User : {} successfully registered data : {}", usersRegisterRequest().getUsersFullName(), responseList);
            return responseList;
        }
    }

    private List<UsersResponse> findUserByUsersIdentityNo() {
        String encrypt = passwordEncoder.encode(identityNoRequest.usersIdentityNo());
        UsersResponse redisUserResponse = redisService.get(encrypt, UsersResponse.class);

        if (redisUserResponse != null) {
            logger.info("User with identity no : {} successfully found from cache, data is {}  ", redisUserResponse.getUsersIdentityNo(), redisUserResponse);
            return List.of(redisUserResponse);
        }else {
            var responseList = usersRepository.findByUsersIdentityNo(identityNoRequest.usersIdentityNo()).stream().map(usersMapper::toDto).toList();

            if (responseList.size() == 1) {
                var jpaUserResponse = responseList.get(0);
                redisService.set(encrypt, jpaUserResponse, 6L, TimeUnit.HOURS);
                logger.info("User with identity no : {} successfully found from jpa, data is {}  ", redisUserResponse.getUsersIdentityNo(), jpaUserResponse);

                return responseList;
            } else {
                var errorMessage  = "User with identity no ending with XXX-XXX-XXX-" + identityNoRequest.usersIdentityNo().substring(9) + "not found";
                var resolveIssue ="Please review the identity number inserted";
                throw throwExceptionAndReport(new UserNotFoundException(errorMessage),errorMessage,resolveIssue);
            }
        }

    }

    private List<UsersResponse> updateUsersPassword() {


        var dbEntity = usersRepository.findByUsersEmailAddress(this.updatePasswordRequest().getUsersEmailAddress());
        if (dbEntity.isPresent()) {
            var user = dbEntity.get();
            user.setUsersPassword(this.updatePasswordRequest().getUsersPassword());
            if (this.updatePasswordRequest().getUsersPassword().equals(this.updatePasswordRequest().getUsersConfirmPassword())) {
                var updateEntity = usersRepository.save(user);
                logger.info("user with email address {} successfully updated password",updateEntity.getUsersEmailAddress());
                return List.of(updateEntity).stream().map(usersMapper::toDto).toList();
            } else {
                var errorMessage = "new password and confirm password don't match";
                var resolveIssue = "Please confirm your new password and confirmation password";
                throw throwExceptionAndReport(new PasswordMisMatchException(errorMessage),errorMessage,resolveIssue);
            }
        } else {
            var errorMessage = "User doesn't exists, check your email address";
            var resolveIssue = "Please check your email address";
            throw throwExceptionAndReport(new UserEmailDoesNotExistException(errorMessage),errorMessage,resolveIssue);
        }
    }


    private List<UsersResponse> findUserById() {

        String encrypt = passwordEncoder.encode(this.getFindByIdRequest().getPkUsersId().toString());
        UsersResponse redisUserResponse = redisService.get(encrypt, UsersResponse.class);

        if (redisUserResponse != null) {
            logger.info("user with id {} successfully retrieved from cache data : {}",redisUserResponse.getId(), redisUserResponse);

            return List.of(redisUserResponse);
        }else {
            var responseList = usersRepository.findById(this.getFindByIdRequest().getPkUsersId()).stream().map(usersMapper::toDto).toList();

            if (responseList.size() == 1) {
                var jpaUserResponse = responseList.get(0);
                redisService.set(encrypt, jpaUserResponse, 6L, TimeUnit.HOURS);
                logger.info("user with id {} successfully retrieved from jpa data : {}",jpaUserResponse.getId(), jpaUserResponse);

                return responseList;
            } else {
                var errorMessage = "user id : " + this.getFindByIdRequest().getPkUsersId() + " not found";
                 var resolveIssue ="Please review id inserted";
                throw throwExceptionAndReport(new UserNotFoundException(errorMessage),errorMessage,resolveIssue);
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
            logger.info("user with name {} successfully retrieved from cache data : {}",redisUserResponse.getUsersFullName(), redisUserResponse);

            return List.of(redisUserResponse);
        }else {
            var responseList = usersRepository.findByUsersFullName(usersFullNameRequest().usersFullName()).stream().map(usersMapper::toDto).toList();

            if (responseList.size() == 1) {
                var jpaUserResponse = responseList.get(0);
                redisService.set(encrypt, jpaUserResponse, 6L, TimeUnit.HOURS);
                logger.info("user with name {} successfully retrieved from jpa data : {}",jpaUserResponse.getUsersFullName(), jpaUserResponse);

                return responseList;
            } else {
                var errorMessage = "user with full name :" + usersFullNameRequest().usersFullName() + " not found";
                var resolveIssue = "Please review the full name inserted";
                throw throwExceptionAndReport(new UserNotFoundException(errorMessage),errorMessage,resolveIssue);
            }
        }

    }

    private boolean redisStatus, passwordStatus;


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
            var entity = new UsersEntity(loginRequest().getUsersEmailAddress(), loginRequest().getUsersPassword());
            redisStatus = false;
            jwtService.generateToken(entity);

            var responseList = usersRepository.findByUsersEmailAddress(entity.getUsersEmailAddress()).stream().map(usersMapper::toDto).toList();


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
                logger.info("user with email {} successfully logged in using cache data : {}",redisUserResponse.getUsersEmailAddress(), redisUserResponse);

                return List.of(redisUserResponse);
            } else {
                logger.info("user with email {} successfully logged in using jpa data : {}",loginRequest().getUsersEmailAddress(), jpaUserResponse);

                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest().getUsersEmailAddress(), loginRequest().getUsersPassword()));
                return List.of(jpaUserResponse);
            }
        } catch (AuthenticationException e) {
            e.printStackTrace();
            if (redisStatus) {
                var errorMessage = "cached data shows change of password ";
                var resolveIssue = "please log in again";
                throw throwExceptionAndReport(new CachedUsersPasswordChangedException(errorMessage),errorMessage,resolveIssue);
            } else {
                if (passwordStatus) {
                    var errorMessage = UsersControllerAdvice.setMessage("password inserted is incorrect");
                    var resolveIssue = "please provide correct password or update password";
                    throw throwExceptionAndReport(new UsersPasswordIncorrectException(errorMessage),errorMessage,resolveIssue);
                } else {
                    var errorMessage = UsersControllerAdvice.setMessage("email address" + loginRequest().getUsersEmailAddress() + " is not found");
                    var resolveIssue = "please provide correct password or register with the email address";
                    throw throwExceptionAndReport(new UserNotFoundException(errorMessage),errorMessage,resolveIssue);
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
