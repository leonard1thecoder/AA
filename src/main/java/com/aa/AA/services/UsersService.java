package com.aa.AA.services;

import com.aa.AA.dtos.*;
import com.aa.AA.entities.UsersEntity;
import com.aa.AA.utils.exceptions.*;
import com.aa.AA.utils.exceptions.controllerAdvices.UsersControllerAdvice;
import com.aa.AA.utils.executors.Execute;
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
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.aa.AA.utils.HandlingLoadingService.*;

@Service
public class UsersService implements Execute<List<UsersResponse>> {

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
                var errorMessage = UsersControllerAdvice.setMessage("Can't register user : User is currently under age can't register");
                UsersControllerAdvice.setResolveIssueDetails("Please ensure you inserting correct identity number");
                throw new UserUnderAgeOf18Exception(errorMessage);
            } else {
                var errorMessage = UsersControllerAdvice.setMessage("Can't register user : User has already been registered");
                UsersControllerAdvice.setResolveIssueDetails("identity number already registered, please login or update password");
                throw new UsersExistsException(errorMessage);
            }

        } else {
            usersEntity.setUsersPassword(passwordEncoder.encode(usersRegisterRequest().getUsersPassword()));
            var entity = usersRepository.save(usersEntity);
            entity.setToken(jwtService.generateToken(entity));
            var entityWithToken = usersRepository.save(entity);
            var entityList = new ArrayList<UsersEntity>();
            entityList.add(entityWithToken);
            var responseList = entityList.stream().map(usersMapper::toDto).toList();
            return responseList;
        }
    }

    private List<UsersResponse> findUserByUsersIdentityNo() {
        String encrypt = passwordEncoder.encode(identityNoRequest.usersIdentityNo());
        UsersResponse redisUserResponse = redisService.get(encrypt, UsersResponse.class);

        if (redisUserResponse != null)
            return List.of(redisUserResponse);
        else {
            var responseList = usersRepository.findByUsersIdentityNo(identityNoRequest.usersIdentityNo()).stream().map(usersMapper::toDto).toList();

            if (responseList.size() == 1) {
                var jpaUserResponse = responseList.get(0);
                redisService.set(encrypt, jpaUserResponse, 6L, TimeUnit.HOURS);
                return responseList;
            } else {
                var errorMessage = UsersControllerAdvice.setMessage("User with identity no ending with XXX-XXX-XXX-" + identityNoRequest.usersIdentityNo().substring(9));
                UsersControllerAdvice.setResolveIssueDetails("Please review the identity no inserted");
                throw new UserNotFoundException(errorMessage);
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
                return List.of(updateEntity).stream().map(usersMapper::toDto).toList();
            } else {
                var errorMessage = UsersControllerAdvice.setMessage("new password and confirm password don't match");
                UsersControllerAdvice.setResolveIssueDetails("Please check your email address");
                throw new PasswordMisMatchException(errorMessage);
            }
        } else {
            var errorMessage = UsersControllerAdvice.setMessage("User doesn't exists, check your email address");
            UsersControllerAdvice.setResolveIssueDetails("Please check your email address");
            throw new UserEmailDoesNotExistException(errorMessage);
        }
    }


    private List<UsersResponse> findUserById() {

        String encrypt = passwordEncoder.encode(this.getFindByIdRequest().getPkUsersId().toString());
        UsersResponse redisUserResponse = redisService.get(encrypt, UsersResponse.class);

        if (redisUserResponse != null)
            return List.of(redisUserResponse);
        else {
            var responseList = usersRepository.findById(this.getFindByIdRequest().getPkUsersId()).stream().map(usersMapper::toDto).toList();

            if (responseList.size() == 1) {
                var jpaUserResponse = responseList.get(0);
                redisService.set(encrypt, jpaUserResponse, 6L, TimeUnit.HOURS);
                return responseList;
            } else {
                var errorMessage = UsersControllerAdvice.setMessage("user id : " + this.getFindByIdRequest().getPkUsersId() + " not found");
                UsersControllerAdvice.setResolveIssueDetails("Please review the id inserted");
                throw new UserNotFoundException(errorMessage);
            }
        }
    }


    private List<UsersResponse> findAllUsers() {
        System.out.println("Testing");
        return usersRepository.findAll().stream().map(usersMapper::toDto).toList();
    }

    private List<UsersResponse> findAllUsersByName() {
        String encrypt = passwordEncoder.encode(usersFullNameRequest().usersFullName());
        UsersResponse redisUserResponse = redisService.get(encrypt, UsersResponse.class);

        if (redisUserResponse != null)
            return List.of(redisUserResponse);
        else {
            var responseList = usersRepository.findByUsersFullName(usersFullNameRequest().usersFullName()).stream().map(usersMapper::toDto).toList();

            if (responseList.size() == 1) {
                var jpaUserResponse = responseList.get(0);
                redisService.set(encrypt, jpaUserResponse, 6L, TimeUnit.HOURS);
                return responseList;
            } else {
                var errorMessage = UsersControllerAdvice.setMessage("user with full name :" + usersFullNameRequest().usersFullName() + " is not found");
                UsersControllerAdvice.setResolveIssueDetails("Please review the full name inserted");
                throw new UserNotFoundException(errorMessage);
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
                return List.of(redisUserResponse);
            } else {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest().getUsersEmailAddress(), loginRequest().getUsersPassword()));
                return List.of(jpaUserResponse);
            }
        } catch (AuthenticationException e) {
            e.printStackTrace();
            if (redisStatus) {
                var errorMessage = UsersControllerAdvice.setMessage("cached data shows change of password ");
                UsersControllerAdvice.setResolveIssueDetails("please provide correct new password");
                throw new CachedUsersPasswordChangedException(errorMessage);
            } else {
                if (passwordStatus) {
                    var errorMessage = UsersControllerAdvice.setMessage("password inserted is incorrect");
                    UsersControllerAdvice.setResolveIssueDetails("please provide correct password or update password");
                    throw new UsersPasswordIncorrectException(errorMessage);
                } else {
                    var errorMessage = UsersControllerAdvice.setMessage("email address" + loginRequest().getUsersEmailAddress() + " is not found");
                    UsersControllerAdvice.setResolveIssueDetails("please provide correct password or register with the email address");
                    throw new UserNotFoundException(errorMessage);
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
