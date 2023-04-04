package com.example.sessionauth.config;

import com.example.sessionauth.dto.EmployeeDTO;
import com.example.sessionauth.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Class is needed for integration test
 * */
@Configuration @Profile(value = "dev")
public class Dummy {
    @Value("${admin.email}")
    private String email;

    private final EmployeeService employeeService;


    @Autowired
    public Dummy(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            var will = new EmployeeDTO();
            will.setEmail(email);
            will.setPassword("password");
            this.employeeService.signupEmployee(will);
        };
    }

}
