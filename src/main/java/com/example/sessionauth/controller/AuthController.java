package com.example.sessionauth.controller;

import com.example.sessionauth.dto.AuthDTO;
import com.example.sessionauth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Public APIs called when registering an employee
     *
     * @param authDTO
     * @return ResponseEntity
     * **/
    @PostMapping(path = "/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthDTO authDTO) {
        return ResponseEntity
                .status(CREATED)
                .body(this.authService.register(authDTO));
    }

    /**
     * Public API that allows an employee to login
     *
     * @param authDTO
     * @param request
     * @param response
     * @return AuthResponse
     * **/
    @PostMapping(path = "/login")
    public ResponseEntity<?> loginEmployee(
            @Valid @RequestBody AuthDTO authDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return new ResponseEntity<>(authService.login(authDTO, request, response), OK);
    }


    /**
     * Protected route only employees with the role ADMIN can hit
     *
     * @param authentication
     * @return String
     * **/
    @GetMapping(path = "/authenticated")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public String getAuthenticated(Authentication authentication) {
        return "Admin name is " + authentication.getName();
    }

    /**
    * Protected route. Any authenticated employee can his this
    *
    * @param authentication
    * @return String
    * **/
    @GetMapping(path = "/employee")
    public String onlyEmployeesCanHitThisRoute(Authentication authentication) {
        return "An Admin or Employee can hit this rout. Employees name is " + authentication.getName();
    }

}
