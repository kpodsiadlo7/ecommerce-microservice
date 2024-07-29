package com.example.gateway.exception;

import com.example.usermanagement.exception.ErrorRecord;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
public class GlobalErrorsHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        ErrorRecord errorRecord;
        HttpStatus status;

        switch (ex) {
            case JwtException jwtException -> {
                log.error("Authorization error {}", ex.getMessage(), ex);
                status = HttpStatus.UNAUTHORIZED;
                errorRecord = new ErrorRecord(ex.getMessage(), status);
            }
            default -> {
                log.error("Unexpected error {}", ex.getMessage(), ex);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                errorRecord = new ErrorRecord(ex.getMessage(), status);
            }
        }

        String errorResponse = convertErrorRecordToJson(errorRecord);
        response.setStatusCode(status);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(errorResponse.getBytes(StandardCharsets.UTF_8))));
    }

    private String convertErrorRecordToJson(ErrorRecord errorRecord) {
        return "{\"message\":\"" + errorRecord.message() + "\", \"status\":\"" + errorRecord.status() + "\"}";
    }
}
