package com.example.sessionauth.config;

import com.example.sessionauth.dto.EmployeeDTO;
import com.example.sessionauth.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/** Class is needed for integration test */
@Configuration @Profile(value = "dev")
public class Dummy {

    @Value("${admin.email}")
    private String email;

    @Value("${employee.email}")
    private String test;

    private final AuthService authService;

    public Dummy(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            var admin = new EmployeeDTO();
            admin.setEmail(email);
            admin.setPassword("password");
            this.authService.register(admin);

            var user = new EmployeeDTO();
            user.setEmail(test);
            user.setPassword("password");
            this.authService.register(user);
        };
    }

}
