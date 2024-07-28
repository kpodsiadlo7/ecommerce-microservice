package com.example.usermanagement;

import com.s2s.JwtUtil;
import com.s2s.KeyProvider;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    @Value("${key.path}")
    private String keyPath;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String uniqueUserId = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            if (JwtUtil.isTokenExpired(jwt)) {
                throw new ExpiredJwtException(null, null, "JWT Token expired!");
            }
            uniqueUserId = JwtUtil.extractUniqueUserId(jwt, KeyProvider.provideKey(keyPath));
        }

        if (uniqueUserId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = userRepository.findByUniqueUserId(uniqueUserId).getUsername();
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (JwtUtil.isTokenExpired(jwt)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}
