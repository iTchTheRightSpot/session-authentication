# Restful Session Authentication and Authorization using java 17, Spring Boot 3.0.4 and MySQL

### Prerequisite

- MySQL for Database

### Quickstart
To use this project, you'll need a MySQL database and a Java IDE with Maven support. 
Clone the repository and import the project into your IDE. Modify the database connection details in the 
application.properties file to match your setup.

```bash or PowerShell
git clone git@github.com:emmanuelU17/restful-session-authentication.git

'To get the application running' ./mvnw clean package spring-boot:run

'Admin employee credential for testing {
  "email" : "admin@admin.com",
  "password" : "password"
} 
'

```

### Description
This project implements session authentication for a web application using Spring Security and MySQL as the session 
storage. Users can sign up and log in using their email and password, and access different parts of the application 
based on their role. The project also includes a custom entity object that maps to the Spring Session schema for easier 
integration.

### Features
* Session authentication: Users can log in and out of the application, and their sessions are stored in a MySQL 
  database for persistence across multiple requests.
* Email and password authentication: Users can create an account with their email and password, and log in securely 
  using Spring Security.
* Role-based authorization: Different parts of the application can be restricted to certain roles, such as employee,
  admin or anonymous.
* Using spring security recommended schema for Session to create an Entity class for SpringSession and 
  SpringSessionAttributes. https://github.com/spring-projects/spring-session/blob/main/spring-session-jdbc/src/main/resources/org/springframework/session/jdbc/schema-mysql.sql
* Logout
* Integration test

### TO IMPLEMENT
* Maximum session (set a constraint on use session).
* Integration test to verify constraint set based on maxSession in the SecurityFilterChain

### Dependencies
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#appendix.configuration-metadata.annotation-processor)
* [Spring Session](https://docs.spring.io/spring-session/reference/)
* [Spring Data JDBC](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#data.sql.jdbc)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#data.sql.jpa-and-spring-data)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#web)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#web.security)
* [Validation](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#io.validation)
