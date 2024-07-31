package com.example.orderservice.auth;

import com.s2s.JwtDetails;
import com.s2s.JwtUtil;
import com.s2s.S2SVerification;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        log.info("Weryfikacja systemu");

        final String authorizationHeader = request.getHeader("Authorization");
        String jwt;
        JwtDetails jwtDetails;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            log.info("bearer started");
            jwt = authorizationHeader.substring(7);
            S2SVerification.verifyRequest(jwt);
            jwtDetails = JwtUtil.extractJwtDetails(jwt, S2SVerification.getSecretKeyBySystem(jwt));
            log.info("jwtDetails: {}", jwtDetails);
        } else {
            throw new JwtException("Token is invalid");
        }

        if (jwtDetails.getRole() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String role = jwtDetails.getRole();
            log.info("Role: {}", role);
            Map<String, Object> headerWithUserId = new HashMap<>();
            headerWithUserId.put("UniqueUserId", jwtDetails.getUserId());

            Authentication authentication = new CustomAuthenticationToken(
                    jwt,
                    null,
                    AuthorityUtils.createAuthorityList("ROLE_" + role),
                    headerWithUserId
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

        private final Map<String, Object> additionalParams;

        public CustomAuthenticationToken(Object principal, Object credentials,
                                         Collection<? extends GrantedAuthority> authorities,
                                         Map<String, Object> additionalParams) {
            super(principal, credentials, authorities);
            this.additionalParams = additionalParams;
        }

        public Map<String, Object> getAdditionalParams() {
            return additionalParams;
        }
    }
}
