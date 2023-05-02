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
storage. Users can sign up and log in using their email and password, and access different parts of the application 
based on their role. This project also includes using Spring Security Session schema for building the right schema for
storing sessions and attributes.

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
* Integration test to verify session fixation set in the SecurityFilterChain

### Dependencies
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#appendix.configuration-metadata.annotation-processor)
* [Spring Session](https://docs.spring.io/spring-session/reference/)
* [Spring Data JDBC](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#data.sql.jdbc)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#data.sql.jpa-and-spring-data)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#web)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#web.security)
* [Validation](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#io.validation)
