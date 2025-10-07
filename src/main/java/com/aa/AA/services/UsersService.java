package com.aa.AA.services;

import com.aa.AA.dtos.UsersRequest;
import com.aa.AA.entities.UsersEntity;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aa.AA.utils.exceptions.ServiceHandlerException;
import com.aa.AA.utils.exceptions.UserUnderAgeOf18Exception;
import com.aa.AA.utils.exceptions.UsersExistsException;
import com.aa.AA.utils.mappers.UsersMapper;
import com.aa.AA.utils.repository.UsersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static com.aa.AA.utils.HandlingLoadingService.*;

@Service
public class UsersService implements Callable<List<UsersRequest>> {

    public static String serviceHandler;


    private UsersRepository usersRepository;
    @Setter
    private static UsersEntity usersEntity;
    @Setter
    private Long usersIdentityNo, pkUsersId;
    @Setter
    private String usersFullName;
    @Setter
    private String usersEmailAddress;
    @Setter
    private String usersPassword;

    private UsersMapper usersMapper;

    public UsersService() {
        super();
    }

    @Autowired
    public UsersService(@Autowired UsersRepository  usersRepository,@Autowired UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
    }

    public static void setUsersEntity(UsersEntity usersEntity) {
        UsersService.usersEntity = usersEntity;
    }

    public void setUsersIdentityNo(Long usersIdentityNo) {
        this.usersIdentityNo = usersIdentityNo;
    }

    public void setUsersFullName(String usersFullName) {
        this.usersFullName = usersFullName;
    }

    public void setPkUsersId(Long pkUsersId) {
        this.pkUsersId = pkUsersId;
    }

    public static void setServiceHandler(String serviceHandler) {
        UsersService.serviceHandler = serviceHandler;
    }

    private List<UsersRequest> registerUsers() {
        var entitiesList = usersRepository.findByUsersIdentityNo(usersIdentityNo);
        entitiesList.forEach(s -> {
            if (s.getUsersIdentityNo() == usersEntity.getUsersIdentityNo()) {
                throw new UsersExistsException("Can't register user : User has already been registered");
            } else if (usersEntity.getUsersAge() < 18) {
                throw new UserUnderAgeOf18Exception("Can't register user : User is currently under age can't register");
            }
        });
        var entity = usersRepository.save(usersEntity);
        var entityList = new ArrayList<UsersEntity>();
        entityList.add(entity);
        return entityList.stream().map(usersMapper::toDto).toList();
    }

    private List<UsersRequest> findUserByUsersIdentityNo() {
        return usersRepository.findByUsersIdentityNo(usersIdentityNo).stream().map(usersMapper::toDto).toList();
    }

    private List<UsersRequest> findUserById() {
        return usersRepository.findById(pkUsersId).stream().map(usersMapper::toDto).toList();
    }

    private List<UsersRequest> findAllUsers() {
        return usersRepository.findAll().stream().map(usersMapper::toDto).toList();
    }

    private List<UsersRequest> findAllUsersByName() {
        return usersRepository.findByUsersFullName(usersFullName).stream().map(usersMapper::toDto).toList();
    }

    private List<UsersRequest> login(){
        return usersRepository.findByUsersEmailAddressAndUsersPassword(usersEmailAddress,usersPassword).stream().map(usersMapper::toDto).toList();
    }

    @Override
    public List<UsersRequest> call()  {
         if(handleServiceHandler(serviceHandler) != "START_SERVICE")
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
