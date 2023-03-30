package com.example.sessionauth.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import static org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final AuthenticationProvider customAuthProvided;

    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    private final AuthenticationEntryPoint authEntryPoint;

    @Autowired
    public SecurityConfig(
            @Qualifier(value = "authProvider") AuthenticationProvider customAuthProvided,
            FindByIndexNameSessionRepository<? extends Session> sessionRepository,
            @Qualifier(value = "authEntryPoint") AuthenticationEntryPoint authEntryPoint
    ) {
        this.customAuthProvided = customAuthProvided;
        this.sessionRepository = sessionRepository;
        this.authEntryPoint = authEntryPoint;
    }


    /**
     * Method is responsible for using custom DB
     * **/
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.authenticationProvider(this.customAuthProvided);
        return managerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // To implement csrf visit spring security
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/v1/auth/signup", "/api/v1/auth/login").permitAll();
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(IF_REQUIRED) //
                        .sessionFixation((sessionFixation) -> sessionFixation.newSession()) //
                        .maximumSessions(1) //
                        .sessionRegistry(sessionRegistry())
                )
                .exceptionHandling((ex) ->
                        ex.authenticationEntryPoint(this.authEntryPoint)
                )
                .logout(out -> out
                        .logoutUrl("/api/v1/auth/logout")
                        .invalidateHttpSession(true) // Invalidate all sessions after logout
                        .deleteCookies("JSESSIONID")
                        .addLogoutHandler(new CustomLogoutHandler(this.sessionRepository))
                        .logoutSuccessHandler((request, response, authentication) ->
                                SecurityContextHolder.clearContext()
                        )
                )
                .build();
    }

    /**
     * Method allows placing constraints on a single user’s ability to log in to your application
     * for better understanding
     * <a href="https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html">...</a>
     * */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    /**
     * Maintains a registry of SessionInformation instances. For better understanding visit
     * <a href="https://github.com/spring-projects/spring-session/blob/main/spring-session-docs/modules/ROOT/examples/java/docs/security/SecurityConfiguration.java">...</a>
     * **/
    @Bean
    public SpringSessionBackedSessionRegistry<? extends Session> sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
    }

}
