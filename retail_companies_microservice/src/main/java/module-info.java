module retail.companies.microservice {
    requires jakarta.persistence;
    requires static lombok;
    requires privileges.microservice;
    requires users.microservice;
    requires spring.beans;
    requires spring.web;
    requires utils.microservice;
    requires spring.security.crypto;
    requires spring.context;
    requires spring.data.jpa;
    requires org.mapstruct;
}