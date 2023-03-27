package com.example.sessionauth.config.security;

import com.example.sessionauth.session.JPASessionRepo;
import com.example.sessionauth.session.SessionAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 * Class is responsible for deleting user session from the DB. It does this by getting Session object ->
 * Using the SESSION_ID to find the SpringSession entity -> Then performs the deleting operation
 * **/
@Component(value = "customLogoutHandler")
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {

    private final JPASessionRepo jpaSessionRepo;

    @Autowired
    public CustomLogoutHandler(@Qualifier(value = "jpaSessionRepo") JPASessionRepo jpaSessionRepo) {
        this.jpaSessionRepo = jpaSessionRepo;
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

            this.jpaSessionRepo
                    .findBySessionID(sessionID) // Find SpringSession entity if it exists
                    .ifPresent(springSession -> {
                        // Get the attribute where SpringSession PRIMARY_ID equals SessionAttribute.SpringSession(PRIMARY_ID)
                        SessionAttributes sessionAttributes = springSession
                                .getAttributes() //
                                .stream() //
                                .filter(attr -> attr.getSpringSession().getId().equals(springSession.getId()))
                                .findFirst() //
                                .orElse(null);

                        // Delete session
                        if (sessionAttributes != null) {
                            springSession.removeAttribute(sessionAttributes);
                            this.jpaSessionRepo.deleteById(springSession.getId());
                            log.info("{} logged out", authentication.getPrincipal().toString());
                        }
                    });
        }

    }

}
