# Restful Session Authentication and Authorization using java 17, Spring Boot 3.1.0 and Redis

### Prerequisite
- Docker

### Quickstart
To use this project, you'll need docker to get the latest versions of MySQL and Redis and Java IDE with Maven support. 
Clone the repository and import the project into your IDE. 

```bash
git clone git@github.com:emmanuelU17/restful-session-authentication.git

'To get the application running' 
* docker compose up -d
* ./mvnw clean spring-boot:run

```

### Description
This project implements session authentication for a web application using Spring Security and Redis as the session 
storage. Users can register and log in using their email and password, and access different parts of the application 
based on their role.

### Features
* Register, Login and Logout using restful APIs.
* Email and password authentication
* Role based authorization
* Redis for storing key value pair
* Testing using test containers

### Dependencies
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#appendix.configuration-metadata.annotation-processor)
* [Spring Session](https://docs.spring.io/spring-session/reference/)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#data.sql.jpa-and-spring-data)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#web)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#web.security)
* [Validation](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#io.validation)
