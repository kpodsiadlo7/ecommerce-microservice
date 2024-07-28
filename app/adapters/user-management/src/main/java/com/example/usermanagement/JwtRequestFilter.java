package com.example.usermanagement;

import com.s2s.JwtDetails;
import com.s2s.JwtUtil;
import com.s2s.KeyProvider;
import com.s2s.S2SVerification;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    @Value("${key.path}")
    private String keyPath;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;
        JwtDetails jwtDetails = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            S2SVerification.verifyRequest(jwt);
            jwtDetails = JwtUtil.extractUniqueUserId(jwt, KeyProvider.provideKey(keyPath));
        }

        if (jwtDetails != null && jwtDetails.getUserId() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String role = jwtDetails.getRole();
            Authentication authentication = new UsernamePasswordAuthenticationToken(jwt, null,
                    AuthorityUtils.createAuthorityList("ROLE_" + role));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
