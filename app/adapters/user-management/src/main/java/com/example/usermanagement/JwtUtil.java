package com.example.usermanagement;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
class JwtUtil {

    String generateToken(final String uniqueUserId) throws IOException {
        // 1 day
        long expirationTime = 86400000;
        return Jwts.builder()
                .setSubject(uniqueUserId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(provideKey())
                .compact();
    }

    private SecretKey provideKey() throws IOException {
        String keyString = new String(Files.readAllBytes(Paths.get("key.txt")));
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    String extractUniqueUserId(String token) throws IOException {
        return extractClaims(token).getSubject();
    }

    Claims extractClaims(String token) throws IOException {
        return Jwts.parserBuilder()
                .setSigningKey(provideKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    boolean isTokenExpired(String token) throws IOException {
        return extractClaims(token).getExpiration().before(new Date());
    }
}