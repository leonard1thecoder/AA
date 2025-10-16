package com.aa.AA.services;

import com.aa.AA.dtos.*;
import com.aa.AA.entities.UsersEntity;
import com.aa.AA.utils.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    private Long pkUsersId;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private UsersMapper usersMapper;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(@Autowired PasswordEncoder passwordEncoder, @Autowired JwtService jwtService, @Autowired AuthenticationManager authenticationManager, @Autowired UsersRepository usersRepository, @Autowired UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
        this.passwordEncoder = passwordEncoder;
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


    public void setPkUsersId(Long pkUsersId) {
        this.pkUsersId = pkUsersId;
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
            return entityList.stream().map(usersMapper::toDto).toList();
        }
    }

    private List<UsersResponse> findUserByUsersIdentityNo() {
        var entity = usersMapper.toEntity(identityNoRequest());
        return usersRepository.findByUsersIdentityNo(entity.getUsersIdentityNo()).stream().map(usersMapper::toDto).toList();
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
        return usersRepository.findById(pkUsersId).stream().map(usersMapper::toDto).toList();
    }

    private List<UsersResponse> findAllUsers() {
        return usersRepository.findAll().stream().map(usersMapper::toDto).toList();
    }

    private List<UsersResponse> findAllUsersByName() {
        var entity = usersMapper.toEntity(usersFullNameRequest());
        return usersRepository.findByUsersFullName(entity.getUsersFullName()).stream().map(usersMapper::toDto).toList();
    }

    private List<UsersResponse> login() {
        var entity = new UsersEntity(loginRequest().getUsersEmailAddress(), loginRequest().getUsersPassword());

        var jwt = jwtService.generateToken(entity);

        System.out.println(jwt);
        try {
            var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(entity.getUsersEmailAddress(), entity.getUsersPassword()));
            if (auth.isAuthenticated())
                return usersRepository.findByUsersEmailAddress(entity.getUsersEmailAddress()).stream().map(usersMapper::toDto).toList();
            else throw new BadCredentialsException("Error with password or username");
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new BadCredentialsException("Error with password or username");

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
