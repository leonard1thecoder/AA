package com.users.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@EnableJpaRepositories(
        basePackages = {"com.privileges.application.repository",
                "com.users.application.repository"})
@EntityScan({"com.privileges.application.entity","com.users.application.entities"})

@ComponentScan(basePackages ={
        "com.utils.application.mailing"
        ,"com.users.application.services"
        ,"com.users.application.controllers"
        ,"com.users.application.configurations"
        ,"com.users.application.mappers"
} )
@SpringBootApplication
public class UsersApplication {
    public static void main(String[] argv) {
        SpringApplication.run(UsersApplication.class, argv);
    }


}
