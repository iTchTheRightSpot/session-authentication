package com.example.sessionauth.controller;

import com.example.sessionauth.dto.EmployeeDTO;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@TestPropertySource("/application-dev.properties")
@Slf4j
class AuthControllerTest {

    @Value(value = "${custom.max.session}")
    private int maxSession;

    @Value(value = "${admin.email}")
    private String adminEmail;

    @Autowired
    private MockMvc mockMvc;

    private EmployeeDTO employeeDTO;

    @BeforeEach
    public void setUp() {
        this.employeeDTO = new EmployeeDTO();
        employeeDTO.setEmail(adminEmail);
        employeeDTO.setPassword("password");
    }

    @Test
    void signUpEmployee() throws Exception {
        var employeeDTO = new EmployeeDTO();
        employeeDTO.setEmail("yes@yes.com");
        employeeDTO.setPassword("password");

        this.mockMvc
                .perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeDTO.convertToJSON().toString())
                )
                .andExpect(status().isCreated())
                .andExpect(content().string("CREATED"));
    }

    @Test
    void loginEmployee() throws Exception {
        this.mockMvc
                .perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeDTO.convertToJSON().toString())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void sign_up_using_existing_email() throws Exception {
        // Given
        var employeeDTO = new EmployeeDTO();
        employeeDTO.setEmail(adminEmail);
        employeeDTO.setPassword("password");

        String errMessage = adminEmail + " already exists";

        // Then
        this.mockMvc
                .perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeDTO.convertToJSON().toString())
                )
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalStateException))
                .andExpect(result ->
                        assertEquals(
                                errMessage,
                                Objects.requireNonNull(result.getResolvedException()).getMessage()
                        )
                );
    }

    /** This test simulates max session set to 1 */
    @Test
    void test_max_session_for_multiple_login_request() throws Exception {
        if (maxSession != 1) {
            return;
        }

        // Simulate login from browser 1
        MvcResult login1 = this.mockMvc
                .perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.employeeDTO.convertToJSON().toString())
                )
                .andExpect(status().isOk())
                .andReturn();

        Cookie cookie1 = login1.getResponse().getCookie("JSESSIONID");
        assert cookie1 != null;

        this.mockMvc
                .perform(get("/api/v1/auth/authenticated")
                        .cookie(cookie1)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Admin name is " + adminEmail));

        // Simulate login from browser 2
        MvcResult login2 = this.mockMvc
                .perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.employeeDTO.convertToJSON().toString())
                )
                .andExpect(status().isOk())
                .andReturn();

        Cookie cookie2 = login2.getResponse().getCookie("JSESSIONID");
        assert cookie2 != null;

        // Expired cookie from browser 1
        this.mockMvc
                .perform(get("/api/v1/auth/authenticated")
                        .cookie(cookie1)
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value("Full authentication is required to access this resource")
                )
                .andExpect(jsonPath("$.httpStatus").value("UNAUTHORIZED"));

        // Valid cookie from browser 2
        this.mockMvc
                .perform(get("/api/v1/auth/authenticated").cookie(cookie2))
                .andExpect(status().isOk());
    }
    
    @Test
    void validate_logout_route() throws Exception {
        // When
        MvcResult login = this.mockMvc
                .perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.employeeDTO.convertToJSON().toString())
                )
                .andExpect(status().isOk())
                .andReturn();

        // Cookie
        Cookie cookie = login.getResponse().getCookie("JSESSIONID");
        assert cookie != null;

        this.mockMvc
                .perform(get("/api/v1/auth/logout")
                        .cookie(cookie)
                )
                .andExpect(status().isOk());

        // Access protected route with invalid cookie
        this.mockMvc
                .perform(get("/api/v1/auth/authenticated").cookie(cookie))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value("Full authentication is required to access this resource")
                )
                .andExpect(jsonPath("$.httpStatus").value("UNAUTHORIZED"));
    }

    @Test
    @WithMockUser(username = "admin@admin.com", password = "password", authorities = {"ADMIN", "EMPLOYEE"})
    void role_admin_access_protected_route() throws Exception {
        this.mockMvc
                .perform(get("/api/v1/auth/authenticated"))
                .andExpect(status().isOk())
                .andExpect(content().string("Admin name is " + adminEmail));
    }

    @Test
    @WithMockUser(username = "test@test.com", password = "password", authorities = {"EMPLOYEE"})
    void role_employee_access_role_admin_route() throws Exception {
        this.mockMvc
                .perform(get("/api/v1/auth/authenticated"))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AccessDeniedException))
                .andExpect(result ->
                        assertEquals(
                                "Access Denied",
                                Objects.requireNonNull(result.getResolvedException()).getMessage()
                        )
                );
    }

    @Test
    void unauthenticated_user_hits_protected_routes() throws Exception {
        // Note error message is Spring boot custom response
        this.mockMvc
                .perform(get("/api/v1/auth/authenticated"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value("Full authentication is required to access this resource")
                )
                .andExpect(jsonPath("$.httpStatus").value("UNAUTHORIZED"));
    }

}