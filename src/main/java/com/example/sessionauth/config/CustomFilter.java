//package com.example.sessionauth.config;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.context.SecurityContextHolderStrategy;
//import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
//import org.springframework.security.web.context.SecurityContextRepository;
//import org.springframework.session.FindByIndexNameSessionRepository;
//import org.springframework.session.Session;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Arrays;
//
//import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
//
//@Component
//public class CustomFilter extends OncePerRequestFilter {
//    private final static Logger LOGGER = LoggerFactory.getLogger(CustomFilter.class);
//    private SecurityContextRepository securityContextRepository =
//            new HttpSessionSecurityContextRepository();
//
//    private final FindByIndexNameSessionRepository sessionRepository;
//
//    public CustomFilter(FindByIndexNameSessionRepository sessionRepository) {
//        this.sessionRepository = sessionRepository;
//    }
//
//    /*
//     * Method validates if request route is a public route.
//     * This is needed for validating JSESSIONID and JWT
//     *
//     * @param HttpServletRequest
//     * @return boolean
//     * */
//    private boolean isUnsecuredRoute(HttpServletRequest req) {
//        String reqURI = req.getRequestURI();
//        //"/api/v1/auth/signup", "/api/v1/auth/login"
//        return reqURI.startsWith("/api/v1/auth/signup") || reqURI.startsWith("/api/v1/auth/login");
//    }
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        LOGGER.info("Custom Auth Filter hit {}", CustomFilter.class);
//        var context = SecurityContextHolder.getContext();
//        Cookie[] cookies = request.getCookies();
//
//        if (cookies != null) {
//            // 0. User is accessing protected API
//            Cookie sessionIDCookie = Arrays
//                    .stream(cookies)
//                    .filter(x -> x.getName().equals("SESSION"))
//                    .findFirst()
//                    .orElse(null);
//
//            LOGGER.info("Cookie " + sessionIDCookie);
//
//            this.securityContextRepository.saveContext(context, request, response);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
