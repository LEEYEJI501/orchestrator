package com.example.orchestrator.util;

import com.example.orchestrator.constants.TokenConstants;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

public class CookieUtil {
    public static void invalidateTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(TokenConstants.ACCESS_TOKEN, "")
                .httpOnly(true)
                .secure(true)
                .path(TokenConstants.COOKIE_PATH)
                .maxAge((int) TokenConstants.COOKIE_MIN_AGE)
                .sameSite(TokenConstants.COOKIE_SAME_SITE)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public static void addTokenCookie(HttpServletResponse response, String cookieValue) {
        ResponseCookie cookie = ResponseCookie.from(TokenConstants.ACCESS_TOKEN, cookieValue)
                .httpOnly(true)
                .secure(true)
                .path(TokenConstants.COOKIE_PATH)
                .maxAge((int) TokenConstants.COOKIE_MAX_AGE)
                .sameSite(TokenConstants.COOKIE_SAME_SITE)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public static void redirectTo(HttpServletResponse response, String location) {
        response.setHeader(HttpHeaders.LOCATION, location);
        response.setStatus(HttpServletResponse.SC_FOUND);
    }
}