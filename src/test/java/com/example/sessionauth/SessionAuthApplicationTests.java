package com.example.sessionauth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class SessionAuthApplicationTests {

    @Container
    public static MySQLContainer<?> container = new MySQLContainer<>
            ("mysql:latest")
            .withDatabaseName("session_db")
            .withUsername("session")
            .withPassword("session");

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        //primary
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @Test
    void contextLoads() { }

}
