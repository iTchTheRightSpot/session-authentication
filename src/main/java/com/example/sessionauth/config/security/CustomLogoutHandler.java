package com.example.sessionauth.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Component;

/**
 * Class is responsible for deleting user session from the DB. It does this by getting Session object ->
 * Using the SESSION_ID to find the SpringSession entity -> Then performs the deleting operation
 * **/
@Component(value = "customLogoutHandler") @Slf4j
public class CustomLogoutHandler implements LogoutHandler {

    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    public CustomLogoutHandler(FindByIndexNameSessionRepository<? extends Session> sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    /**
     * Method responsible for deleting user session post logout
     *
     * @param request
     * @param response
     * @param authentication
     * @return void
     * */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        HttpSession requestSession = request.getSession(false);

        if (requestSession != null) {
            String sessionID = requestSession.getId();
            Session session = this.sessionRepository.findById(sessionID);
            boolean bool = session
                    .getAttributeOrDefault(
                            FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME,
                            authentication.getName()
                    )
                    .isEmpty();

            // Validate the user requesting to log out is actually the user
            if (!bool) {
                this.sessionRepository.deleteById(sessionID);
            }
        }
    }

}
