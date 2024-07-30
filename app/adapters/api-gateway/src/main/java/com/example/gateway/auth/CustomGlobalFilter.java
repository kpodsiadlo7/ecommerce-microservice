package com.example.gateway.auth;

import com.example.apigateway.feign.UserManagementClient;
import com.s2s.JwtDetails;
import com.s2s.JwtUtil;
import com.s2s.KeyProvider;
import com.s2s.S2SVerification;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomGlobalFilter implements WebFilter {

    private final UserManagementClient userManagementClient;
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
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            log.warn("token {}", token);
        } else {
            return Mono.error(new JwtException("Token is invalid!"));
        }

        try {
            if (checkTokenCondition() && checkUser()) {
                JwtDetails jwtDetails = JwtUtil.extractJwtDetails(token, getSecretKeyFromTrustedStore("user-management"));
                String uniqueUserId = jwtDetails.getUserId();
                String newToken = JwtUtil.generateToken("gateway", uniqueUserId, getSecretKey(), "SYSTEM");
                //todo
                log.info("api-gateway token {}", newToken);

                log.info("Token is valid, new token generated");
                exchange = replaceUserTokenWithGatewayToken(exchange, newToken, uniqueUserId);
            }
        } catch (Exception e) {
            return Mono.error(new Exception("Error: ", e));
        }
        return chain.filter(exchange);
    }

    private boolean checkUser() throws IOException {
        String systemToken = JwtUtil.generateToken("gateway", "gateway", getSecretKey(), "SYSTEM");
        return userManagementClient.checkUser(token, "Bearer " + systemToken);
    }

    private SecretKey getSecretKeyFromTrustedStore(String systemName) throws IllegalAccessException {
        if (S2SVerification.checkSystemIsRecognized(systemName)) {
            return S2SVerification.getSecretSystemKey(systemName);
        }
        throw new IllegalAccessException("System is not recognized");
    }

    private boolean checkTokenCondition() {
        log.info("check condition");
        return token != null && S2SVerification.verifyRequest(token);
    }

    private String extractTokenFromRequest(ServerHttpRequest request) {
        return request.getHeaders().getFirst("Authorization");
    }

    private SecretKey getSecretKey() throws IOException {
        return KeyProvider.provideKey(keyPath);
    }

    private ServerWebExchange replaceUserTokenWithGatewayToken(ServerWebExchange exchange, String newToken, String uniqueUserId) {
        log.info("replace user token with gateway token");
        ServerHttpRequest request = exchange.getRequest().mutate()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + newToken)
                .header("PublicUserId", uniqueUserId)
                .build();

        return exchange.mutate().request(request).build();
    }
}
