package com.users.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.docker.compose.lifecycle.DockerComposeProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableJpaRepositories(
        basePackages = {
                "com.users.application.repository"})
@EntityScan({"com.privileges.application.entity","com.users.application.entities"})

@ComponentScan(basePackages ={
        "com.utils.application.mailing"
        ,"com.users.application.services"
        ,"com.users.application.controllers"
        ,"com.users.application.configurations"
        ,"com.users.application.mappers",
        "com.utils.application",
        "com.utils.application.configurations",
        "com.privileges.application.service"
        
        
} )
@SpringBootApplication
public class UsersApplication {
    public static void main(String[] argv) {
        System.out.println("Developer name: Sizolwakhe Leonard Mthimunye \nEmail address : leonard1thecoder@gmail.com \nGithub repository : https://github.com/leonard1thecoder/AA");
        SpringApplication.run(UsersApplication.class, argv);
    }


}
