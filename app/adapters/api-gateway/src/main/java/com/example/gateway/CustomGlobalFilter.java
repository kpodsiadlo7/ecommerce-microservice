package com.example.gateway;

import com.s2s.JwtUtil;
import com.s2s.KeyProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Configuration
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @Value("${key.path}")
    private String keyPath;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String method = String.valueOf(exchange.getRequest().getMethod());

        if (("/register".equalsIgnoreCase(path) || "/login".equalsIgnoreCase(path)) && "POST".equalsIgnoreCase(method)) {
            return chain.filter(exchange);
        }

        try {
            if (checkCondition(exchange)) {
                String uniqueUserId = "exampleUniqueUserId";
                String newToken = JwtUtil.generateToken("gateway", uniqueUserId, KeyProvider.provideKey(keyPath));
                ServerWebExchange updatedExchange = updateHeader(exchange, newToken, uniqueUserId);

                return chain.filter(updatedExchange);
            } else {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }
    }

    private static ServerWebExchange updateHeader(ServerWebExchange exchange, String newToken, String uniqueUserId) {
        ServerWebExchange updatedExchange = exchange.mutate()
                .request(r -> r.headers(headers -> {
                    headers.set(HttpHeaders.AUTHORIZATION, newToken);
                    headers.set("uniqueUserId", uniqueUserId);
                }))
                .build();
        return updatedExchange;
    }

    private boolean checkCondition(ServerWebExchange exchange) throws IOException {
        String token = extractTokenFromRequest(exchange);
        return token != null && JwtUtil.isTokenExpired(token);
    }

    private String extractTokenFromRequest(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst("Authorization");
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
