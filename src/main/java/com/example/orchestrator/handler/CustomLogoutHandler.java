package com.example.orchestrator.handler;

import com.example.orchestrator.constants.TokenConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TokenConstants.ACCESS_TOKEN.equals(cookie.getName()) || "JSESSIONID".equals(cookie.getName())) {
                    cookie.setValue(null);
                    cookie.setMaxAge((int) TokenConstants.COOKIE_MIN_AGE);
                    cookie.setPath(TokenConstants.COOKIE_PATH);
                    response.addCookie(cookie);
                }
            }
        }
        request.getSession().invalidate();
    }
}

