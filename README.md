# Restful Session Authentication and Authorization using java 17, Spring Boot 3.0.4 and MySQL

### Prerequisite

- Docker

### Quickstart
To use this project, you'll need docker to get the latest MySQL DB and Java IDE with Maven support. 
Clone the repository and import the project into your IDE. 

```bash or PowerShell
git clone git@github.com:emmanuelU17/restful-session-authentication.git

'To get the application running' 
* docker compose up -d
* ./mvnw clean spring-boot:run

'Admin employee credential for testing {
  "email" : "admin@admin.com",
  "password" : "password"
} 
'

```

### Description
This project implements session authentication for a web application using Spring Security and MySQL as the session 
storage. Users can register and log in using their email and password, and access different parts of the application 
based on their role. This project also includes using JPA to build schema for User & Role Objects, using Spring 
Security recommended schema for storing sessions and attributes and finally using test containers to validate
functionalities.

### Features
* Register, Login and Logout using restful APIs.
* Email and password authentication
* Accessibility to routes/APIs based on roles (Authorization)
* Using spring security recommended schema for jdbc sessions
  https://github.com/spring-projects/spring-session/blob/main/spring-session-jdbc/src/main/resources/org/springframework/session/jdbc/schema-mysql.sql
* Integration test using test containers

### TO IMPLEMENT
* Frontend to validate visual functionality (Angular)
* CSRF protection
* Integration test to verify session fixation set in the SecurityFilterChain

### Dependencies
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#appendix.configuration-metadata.annotation-processor)
* [Spring Session](https://docs.spring.io/spring-session/reference/)
* [Spring Data JDBC](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#data.sql.jdbc)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#data.sql.jpa-and-spring-data)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#web)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#web.security)
* [Validation](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#io.validation)
