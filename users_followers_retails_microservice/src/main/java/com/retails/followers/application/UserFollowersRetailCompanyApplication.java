package com.retails.followers.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableJpaRepositories(
        basePackages = {
                "com.users.application.repository"
        ,"com.retails.application.repository"
        ,"com.retails.followers.application.repository"})
@EntityScan({"com.retails.application.entity"
        ,"com.users.application.entities"
        ,"com.retails.followers.application.entities"})

@ComponentScan(basePackages ={
        "com.utils.application.mailing"
        ,"com.retails.followers.application.service"
        ,"com.retails.followers.application.controller"
        ,"com.retails.followers.application.configuration"
        , "com.utils.application"
        , "com.utils.application.configurations",
} )
@SpringBootApplication
public class UserFollowersRetailCompanyApplication {
    public static void main(String[] args) {

        System.out.print("Hello and welcome!");

        SpringApplication.run(UserFollowersRetailCompanyApplication.class, args);

    }
}