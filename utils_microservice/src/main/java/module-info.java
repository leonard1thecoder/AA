module utils.microservice {
    exports com.utils.application;
    exports com.utils.application.globalExceptions;
    exports com.utils.application.globalExceptions.errorResponse;
    exports com.utils.application.controllerAdvice;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires spring.beans;
    requires spring.context;
    requires spring.data.redis;
    requires org.slf4j;
    requires spring.security.crypto;
    requires spring.web;
    requires users.microservice;
}