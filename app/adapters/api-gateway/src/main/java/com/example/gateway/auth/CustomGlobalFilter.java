package com.example.gateway.auth;

import com.example.apigateway.UserManagementClient;
import com.s2s.JwtDetails;
import com.s2s.JwtUtil;
import com.s2s.KeyProvider;
import com.s2s.S2SVerification;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class CustomGlobalFilter implements WebFilter {

    private final UserManagementClient userManagementClient;
    private static final Logger log = LoggerFactory.getLogger(CustomGlobalFilter.class);
    @Value("${key.path}")
    private String keyPath;
    private String token;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String requestURI = request.getURI().getPath();
        if ((requestURI.equalsIgnoreCase("/register") || requestURI.equalsIgnoreCase("/login"))) {
            return chain.filter(exchange);
        }

        log.warn("filter started");
        final String authorizationHeader = extractTokenFromRequest(request);
        if (authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            log.warn("token {}", token);
        } else {
            return Mono.error(new IllegalArgumentException("Token is invalid!"));
        }

        try {
            if (checkTokenCondition() && checkUser(token)) {
                JwtDetails jwtDetails = JwtUtil.extractUniqueUserId(token, getSecretKeyFromTrustedStore("user-management"));
                String uniqueUserId = jwtDetails.getUserId();
                String newToken = JwtUtil.generateToken("gateway", uniqueUserId, getSecretKey(), "SYSTEM");
                log.warn("Token is valid, new token generated");
                exchange = replaceUserTokenWithGatewayToken(exchange, newToken, uniqueUserId);
            } else {
                return Mono.error(new JwtException("Authorization header is missing or invalid"));
            }
        } catch (Exception e) {
            return Mono.error(new JwtException("Invalid token", e));
        }
        return chain.filter(exchange);
    }

    private boolean checkUser(final String token) {
        return userManagementClient.checkUser(token);
    }

    private SecretKey getSecretKeyFromTrustedStore(String systemName) throws IllegalAccessException {
        if (S2SVerification.checkSystemIsRecognized(systemName)) {
            log.info("Klucz {}", S2SVerification.getSecretSystemKey(systemName));
            return S2SVerification.getSecretSystemKey(systemName);
        }
        throw new IllegalAccessException("System is not recognized");
    }

    private boolean checkTokenCondition() {
        log.info("check condition");
        return token != null && !S2SVerification.verifyRequest(token);
    }

    private String extractTokenFromRequest(ServerHttpRequest request) {
        return request.getHeaders().getFirst("Authorization");
    }

    private SecretKey getSecretKey() throws IOException {
        return KeyProvider.provideKey(keyPath);
    }

    private ServerWebExchange replaceUserTokenWithGatewayToken(ServerWebExchange exchange, String newToken, String uniqueUserId) {
        ServerHttpRequest request = exchange.getRequest().mutate()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + newToken)
                .header("PublicUserId", uniqueUserId)
                .build();

        return exchange.mutate().request(request).build();
    }
}
