package com.rdx.rdxserver.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.annotations.Comment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final String[] excludedPaths = { "/api/user/login", "/api/user/register"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Get the requested path
        String path = request.getServletPath();

        // Check if the path is excluded from the authentication check
        for (String excludedPath : excludedPaths) {
            if (path.startsWith(excludedPath)) {
                // If the path is excluded, allow access to the requested resource
                filterChain.doFilter(request, response);
                return;
            }
        }

        // If the path is not excluded, proceed with the authentication check as before
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authorizationHeader.substring(7);

        try {
            Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

}
