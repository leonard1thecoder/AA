package services;

import entities.UsersEntity;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ServiceBuilder;
import utils.exceptions.ServiceHandlerException;
import utils.exceptions.UserUnderAgeOf18Exception;
import utils.exceptions.UsersExistsException;
import utils.repository.UsersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Service
public class UsersService implements Callable<List<UsersEntity>>, ServiceBuilder {
    @Setter
    public static String serviceHandler;

    @Autowired
    UsersRepository usersRepository;
    @Setter
    private static UsersEntity usersEntity;
    @Setter
    private Long usersIdentityNo,pkUsersId;
    @Setter
    private String usersFullName;

    private List<UsersEntity> registerUsers() {
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
        return entityList;
    }

    private List<UsersEntity> findUserByUsersIdentityNo(){
        return usersRepository.findByUsersIdentityNo(usersIdentityNo);
    }

    private List<UsersEntity> findUserById(){
        return usersRepository.findById(pkUsersId).stream().toList();
    }

    private List<UsersEntity> findAllUsers(){
        return usersRepository.findAll();
    }

    private List<UsersEntity> findAllUsersByName(){
        return usersRepository.findByUsersFullName(usersFullName);
    }

    @Override
    public List<UsersEntity> call() throws Exception {

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
            default:
                throw new ServiceHandlerException("Failed execute service due to incorrect service string");
        }
    }
}
