package com.users.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(
        basePackages = {"com.privileges.application.repository",
                "com.users.application.repository"})
@EntityScan({"com.privileges.application.entity","com.users.application.entities"})
@SpringBootApplication
public class UsersApplication {
    public static void main(String[] argv) {
        SpringApplication.run(UsersApplication.class, argv);
    }


}
