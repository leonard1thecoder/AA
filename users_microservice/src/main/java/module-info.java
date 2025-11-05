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
    requires org.slf4j;
    requires spring.security.crypto;
    requires com.fasterxml.jackson.annotation;
    requires jjwt.api;
    requires org.mapstruct;
    requires utils.microservice;
    requires spring.web;
    requires spring.security.config;
    requires org.apache.tomcat.embed.core;
    requires spring.security.web;
    requires spring.core;

    opens com.users.application.entities;
    opens com.users.application;
    opens com.users.application.repository;
    exports com.users.application.exceptions;
}