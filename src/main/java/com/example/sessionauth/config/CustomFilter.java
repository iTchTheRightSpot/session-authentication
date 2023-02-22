package com.example.sessionauth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class CustomFilter extends OncePerRequestFilter {
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomFilter.class);

    private final FindByIndexNameSessionRepository sessionRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        LOGGER.info("Custom Auth Filter hit {}", CustomFilter.class);

        if (isUnsecuredRoute(request)) {
            LOGGER.info("Public route hit");
            filterChain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = request.getCookies();

        // 0. User is accessing protected API
        Cookie sessionIDCookie = Arrays
                .stream(cookies)
                .filter(x -> x.getName().equals("JSESSIONID"))
                .findFirst()
                .orElse(null);


        if (sessionIDCookie != null) {
            var context = SecurityContextHolder.getContext().getAuthentication();
            if (context != null) {
//                String principal = (String) context.getPrincipal();
                String sessionID = sessionIDCookie.getValue();
                Session session = sessionRepository.findById(sessionID);
                String sessionName = session.getRequiredAttribute("principal_name");

//                if (!session.isExpired() || !principal.equals(sessionName)) {
//                    response.setStatus(401);
//                    return;
//                }
            }

        }

        filterChain.doFilter(request, response);

    }

    /*
     * Method validates if request route is a public route.
     * This is needed for validating JSESSIONID and JWT
     *
     * @param HttpServletRequest
     * @return boolean
     * */
    private boolean isUnsecuredRoute(HttpServletRequest req) {
        String reqURI = req.getRequestURI();
        return reqURI.startsWith("/api/public/");
    }


}
