package com.example.sessionauth.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Component;

/**
 * Class is responsible for deleting user session from the DB. It does this by getting Session object ->
 * Using the SESSION_ID to find the SpringSession entity -> Then performs the deleting operation
 * **/
@Component(value = "customLogoutHandler")
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {

    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    @Autowired
    public CustomLogoutHandler(FindByIndexNameSessionRepository<? extends Session> sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    /**
     * This method is responsible for removing the user session from the DB.
     * 'request.getSession(false) means I am fetching session from the request if it exists'
     *
     * @param request
     * @param response
     * @param authentication
     * @return void
     * */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            String sessionID = session.getId();
            Session sessionRepositoryById = this.sessionRepository.findById(sessionID);

            if (sessionRepositoryById != null) {
                this.sessionRepository.deleteById(sessionID);
            }
        }

    }

}
