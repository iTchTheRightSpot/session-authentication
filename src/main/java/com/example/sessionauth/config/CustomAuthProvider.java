package com.example.sessionauth.config;

import com.example.sessionauth.entity.Employee;
import com.example.sessionauth.entity.userdetails.EmployeeDetails;
import com.example.sessionauth.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomAuthProvider implements AuthenticationProvider {
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomAuthProvider.class);
    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        LOGGER.info("Custom Auth Provider {}", CustomAuthProvider.class);
        Employee employee = employeeService.findEmployeeByEmail(email);

        // Validate if credentials is an employee
        UserDetails employeeDetails = new EmployeeDetails(employee);
        if (passwordEncoder.matches(password, employeeDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(email, password, employeeDetails.getAuthorities());
        }

        throw new UsernameNotFoundException("Invalid email or password");
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
