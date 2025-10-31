module privileges.microservice {
    opens com.privileges.application.entity;
    opens com.privileges.application.repository;
    exports com.privileges.application.entity;
    requires jakarta.persistence;
    requires static lombok;
    requires spring.data.jpa;
    requires spring.context;
    requires org.hibernate.orm.core;
}