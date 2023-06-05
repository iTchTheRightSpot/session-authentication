package com.example.sessionauth.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.stereotype.Component;

@Component(value = "customLogoutHandler")
public class CustomLogoutHandler implements LogoutHandler {

    private final FindByIndexNameSessionRepository<? extends Session> redisIndexedSessionRepository;

    public CustomLogoutHandler(RedisIndexedSessionRepository redisIndexedSessionRepository) {
        this.redisIndexedSessionRepository = redisIndexedSessionRepository;
    }

    /**
     * Method responsible for deleting user session from redis
     *
     * @param request
     * @param response
     * @param authentication
     * @return void
     * */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String id = request.getSession(false).getId();
        if (id != null && this.redisIndexedSessionRepository.findById(id) != null) {
            this.redisIndexedSessionRepository.deleteById(id);
        }
    }

}
