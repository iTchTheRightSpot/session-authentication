package com.example.sessionauth.config.security;

import com.example.sessionauth.entity.Employee;
import com.example.sessionauth.entity.userdetails.EmployeeDetails;
import com.example.sessionauth.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Class is responsible for validating request based on info stored in database
 * */
@Component(value = "authProvider")
@AllArgsConstructor
@Slf4j
public class CustomAuthProvider implements AuthenticationProvider {
    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("{} hit", CustomAuthProvider.class);

        String email = (String) authentication.getPrincipal();
        String password = authentication.getCredentials().toString();
        Employee employee = employeeService.findEmployeeByEmail(email);

        EmployeeDetails employeeDetails = new EmployeeDetails(employee);

        // validate user is confirmed, not locked etc
        boolean status = employeeDetails.isEnabled()
                && !employeeDetails.isAccountNonExpired()
                && !employeeDetails.isAccountNonLocked()
                && !employeeDetails.isCredentialsNonExpired();

        // return is status is true and password matches
        if (status && passwordEncoder.matches(password, employeeDetails.getPassword())) {
            log.info("Custom Auth Provider {}", CustomAuthProvider.class);
            return UsernamePasswordAuthenticationToken
                    .authenticated(email, null, employeeDetails.getAuthorities());
        }

        throw new UsernameNotFoundException("Invalid email or password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
