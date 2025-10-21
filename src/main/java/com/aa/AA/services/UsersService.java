package com.aa.AA.services;

import com.aa.AA.dtos.*;
import com.aa.AA.entities.UsersEntity;
import com.aa.AA.utils.exceptions.*;
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
public class UsersService implements Callable<List<UsersResponse>> {

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
    public UsersService(@Autowired RedisService redisService, @Autowired PasswordEncoder passwordEncoder, @Autowired JwtService jwtService, @Autowired AuthenticationManager authenticationManager, @Autowired UsersRepository usersRepository, @Autowired UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.redisService = redisService;
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
            var entity = entitiesList.get();
            if (entity.getUsersIdentityNo() == request.getUsersIdentityNo()) {
                throw new UsersExistsException("Can't register user : User has already been registered");
            } else if (request.getUsersAge() < 18) {
                throw new UserUnderAgeOf18Exception("Can't register user : User is currently under age can't register");
            } else {
                throw new RuntimeException("BLocker");
            }
        } else {
            usersEntity.setUsersPassword(passwordEncoder.encode(usersRegisterRequest().getUsersPassword()));
            var entity = usersRepository.save(usersEntity);
            entity.setToken(jwtService.generateToken(entity));

            var entityWithToken = usersRepository.save(entity);
            var entityList = new ArrayList<UsersEntity>();
            entityList.add(entityWithToken);
            var responseList = entityList.stream().map(usersMapper::toDto).toList();
            redisService.set("_usersCache", responseList, 3L, TimeUnit.MINUTES);
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
                return null;
            }
        }

    }

    private List<UsersResponse> updateUsersPassword() {

        var entity = usersMapper.toEntity(this.updatePasswordRequest());

        var dbEntity = usersRepository.findByUsersEmailAddress(entity.getUsersEmailAddress());
        if (dbEntity.isPresent()) {
            var user = dbEntity.get();
            user.setUsersPassword(entity.getUsersPassword());
            var updateEntity = usersRepository.save(user);
            return List.of(updateEntity).stream().map(usersMapper::toDto).toList();
        } else if (dbEntity.isEmpty()) {
            throw new UserEmailDoesNotExist("User doesn't exists, check your email address");
        } else {
            throw new RuntimeException("Many users exists with same email");
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
                return null;
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
                return null;
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
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest().getUsersPassword(), loginRequest().getUsersPassword()));
                return List.of(redisUserResponse);
            } else {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest().getUsersPassword(), loginRequest().getUsersPassword()));
                return List.of(jpaUserResponse);
            }
        } catch (AuthenticationException e) {
            e.printStackTrace();
            if (redisStatus)
                throw new CachedUsersPasswordChangedException("Cached user password changed, auth failed : " + e.getMessage());
            else {
                if (passwordStatus)
                    throw new UsersPasswordIncorrectException("Recorded password  in DB is incorrect : " + e.getMessage());
                else
                    throw new UsersExistsException("User is not registered");
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
}
