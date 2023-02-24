package com.example.sessionauth.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import static org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final CustomAuthProvider customAuthProvided;
    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    /*
     * Method is responsible for using custom DB
     * */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.authenticationProvider(customAuthProvided);
        return managerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/v1/auth/signup", "/api/v1/auth/login").permitAll();
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(sessions ->
                        sessions
                                .sessionCreationPolicy(IF_REQUIRED) //
                                .sessionFixation().migrateSession()
                                .maximumSessions(1) //
                )
//                .addFilterBefore(new CustomFilter(sessionRepository), UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    /*
     * Method allows placing constraints on a single user’s ability to log in to your application
     * for better understanding
     * https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html
     * */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }

}
