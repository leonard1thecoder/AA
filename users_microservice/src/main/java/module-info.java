module users.microservice {
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires static lombok;
    requires jakarta.persistence;
    requires spring.security.core;
    requires privileges.microservice;
    requires spring.data.jpa;
    requires spring.context;
    requires org.hibernate.orm.core;
    requires spring.beans;

    opens com.users.application.entities;
    opens com.users.application;
    opens com.users.application.repository;
}