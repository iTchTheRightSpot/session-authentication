package com.example.sessionauth.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {

    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;


    /*
     * Method is responsible for deleting using sessions in the DB when user signs out.
     * The only downside of this approach is if max session > 2 all devices will be logged out.
     * Link below for better understanding
     * https://stackoverflow.com/questions/42083519/how-to-remove-existing-sessions-by-specific-principal-name
     * */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        sessionRepository
                .findByPrincipalName(authentication.getPrincipal().toString())
                .keySet().forEach(sessionRepository::deleteById);

    }


}
