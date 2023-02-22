package com.example.sessionauth.controller;

import com.example.sessionauth.dto.EmployeeDTO;
import com.example.sessionauth.service.AuthService;
import com.example.sessionauth.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(path = "/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final EmployeeService employeeService;

    /*
     * Public APIs needed to sign up employees
     *
     * @param employeeDTO
     * @return ResponseEntity
     * */
    @PostMapping(path = "/signup")
    public ResponseEntity<?> signUpEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        LOGGER.info("Employee sign up called from {}", AuthController.class);
        employeeService.signupEmployee(employeeDTO);
        return ResponseEntity
                .status(CREATED)
                .body("CREATED");
    }

    /*
     * Public API that allows an employee to login
     *
     * @param employeeDTO
     * @return AuthResponse
     * */
    @PostMapping(path = "/login")
    public ResponseEntity<?> loginEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        LOGGER.info("Employee logged in called from {}", AuthController.class);
        return authService.loginEmployee(employeeDTO);
    }

    /*
    *
    * */
    @GetMapping(path = "authenticated")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    public String getAuthenticated(Authentication authentication) {
        return "Authenticated " + authentication.getPrincipal();
    }



}
