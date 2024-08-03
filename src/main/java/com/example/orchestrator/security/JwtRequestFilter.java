package com.example.orchestrator.security;

import com.example.orchestrator.service.AuthService;
import com.example.orchestrator.service.CustomUserDetailService;
import com.example.orchestrator.util.JsonUtil;
import com.example.orchestrator.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // 추가된 임포트 구문
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import io.jsonwebtoken.ExpiredJwtException;
import com.example.grpc.*;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailService customUserDetailsService;

    @Autowired
    private AuthService authService;

    @Autowired
    private JsonUtil jsonUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            Long userId = extractUserIdFromRequest(request);
            String jwt = extractJwtFromRequest(request);

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.customUserDetailsService.loadUserByUserId(userId);

                if (jwtUtil.validateToken(jwt, userId)) {
                    setAuthenticationForUser(userDetails, request);
                }
            }
        } catch (ExpiredJwtException e) {
            String jwt = extractJwtFromRequest(request);
            Claims claims = e.getClaims();
            Long userId = Long.parseLong(claims.getSubject());

            Response tokenResponse = authService.refreshToken(userId);
            String newAccessToken = extractNewAccessTokenFromResponse(tokenResponse);

            if (newAccessToken != null) {
                updateJwtInRequest(request, newAccessToken);
                UserDetails userDetails = this.customUserDetailsService.loadUserByUserId(userId);

                if (jwtUtil.validateToken(newAccessToken, userId)) {
                    setAuthenticationForUser(userDetails, request);
                }
            } else {
                handleExpiredToken(response);
                return;
            }
        } catch (Exception e) {
            handleInvalidToken(response);
            return;
        }

        chain.doFilter(request, response);
    }

    private void handleExpiredToken(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("JWT token is expired");
    }

    private void handleInvalidToken(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("Invalid JWT token");
    }

    private void setAuthenticationForUser(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Long extractUserIdFromRequest(HttpServletRequest request) {
        String jwt = extractJwtFromRequest(request);
        if (jwt != null) {
            return jwtUtil.getUserIdFromToken(jwt);
        }
        return null;
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    private String extractNewAccessTokenFromResponse(Response response) {
        return response != null ? jsonUtil.getValueByKey(response.getResult(), "accessToken") : null;
    }

    private void updateJwtInRequest(HttpServletRequest request, String newAccessToken) {
        request.setAttribute("Authorization", "Bearer " + newAccessToken);
    }
}
