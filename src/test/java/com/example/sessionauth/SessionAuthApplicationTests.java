package com.example.sessionauth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class SessionAuthApplicationTests {

//    @Container
//    public static MySQLContainer<?> primary = new MySQLContainer<>
//            ("mysql:8")
//            .withDatabaseName("dummy")
//            .withUsername("root")
//            .withPassword("root");
//
//    @Container
//    public static MySQLContainer<?> session = new MySQLContainer<>
//            ("mysql:8")
//            .withDatabaseName("session")
//            .withUsername("root")
//            .withPassword("root");

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        //primary
//        registry.add("spring.datasource.url",primary::getJdbcUrl);
//        registry.add("spring.datasource.username", primary::getUsername);
//        registry.add("spring.datasource.password", primary::getPassword);
//        //session
//        registry.add("session.datasource.url",session::getJdbcUrl);
//        registry.add("session.datasource.username",session::getUsername);
//        registry.add("session.datasource.password",session::getPassword);
    }

    @Test
    void contextLoads() {
    }

}
