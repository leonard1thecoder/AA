module utils.microservice {
    exports com.utils.application;
    exports com.utils.application.globalExceptions;
    exports com.utils.application.globalExceptions.errorResponse;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires spring.beans;
    requires spring.context;
    requires spring.data.redis;
    requires org.slf4j;
}