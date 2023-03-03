# Restful Session Authentication and Authorization using java 17, Spring Boot 3.0.2 and MySQL

### Description
This project should explain using Spring Security to explain/implement role based authentication and authorization.
I would be implementing the logic of a user attempts to sign in, if the credentials match details in store in the
database (MySQL), a session is created and added to the SecurityContextHolder. Each user will have a max session of 1 
meaning user can't be signed in on multiple browsers at once. 

To explain further, I created to separate MySQL databases. A primary database (called Dummy) to handle the applications 
needs and a session database (called SessION) to handles sessions when created. The session management policy is 
'IF_REQUIRED' because I want spring security to manage session for employee/users that have been authenticated. Below is
a visual representation of both databases.

### IMPLEMENTED
* Connecting multiple datasource (Databases)
* Using spring recommended schema for JDBC Session https://github.com/spring-projects/spring-session/blob/main/spring-session-jdbc/src/main/resources/org/springframework/session/jdbc/schema-mysql.sql
* Persisted authenticated users in SecurityContextHolderStrategy and SecurityContextRepository
* Logout route (Delete sessions from SecurityContextHolder).
* Maximum session (If a user tries signing in though user still has a valid session in the
  SecurityContextHolder, new session is replaced with the old session).

### Dependencies
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/#appendix.configuration-metadata.annotation-processor)
* [Spring Session](https://docs.spring.io/spring-session/reference/)
* [Spring Data JDBC](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/#data.sql.jdbc)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/#data.sql.jpa-and-spring-data)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/#web)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/#web.security)
* [Validation](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/#io.validation)
