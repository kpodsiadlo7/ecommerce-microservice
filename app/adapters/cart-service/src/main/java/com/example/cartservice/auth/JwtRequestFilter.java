package com.example.cartservice.auth;

import com.s2s.S2SVerification;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("start filter");
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null) throw new ServletException("Token is not exists!");

        if (authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            S2SVerification.verifyRequest(jwt);
        } else {
            throw new ServletException("It's not bearer token!");
        }
        filterChain.doFilter(request, response);
    }
}
