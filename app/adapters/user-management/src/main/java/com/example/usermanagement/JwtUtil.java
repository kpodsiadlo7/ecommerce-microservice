package com.example.usermanagement;

import com.s2s.KeyProvider;
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
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
class JwtUtil {

    private static final String KEY_PATH = "key.txt";

    String generateToken(final String uniqueUserId) throws IOException {
        // 1 day
        Map<String, Object> claims = new HashMap<>();
        claims.put("system", "user-management");
        long expirationTime = 86400000;
        return Jwts.builder()
                .setSubject(uniqueUserId)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(KeyProvider.provideKey(KEY_PATH))
                .compact();
    }

    String extractUniqueUserId(String token) throws IOException {
        return extractClaims(token).getSubject();
    }

    Claims extractClaims(String token) throws IOException {
        return Jwts.parserBuilder()
                .setSigningKey(KeyProvider.provideKey(KEY_PATH))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    boolean isTokenExpired(String token) throws IOException {
        return extractClaims(token).getExpiration().before(new Date());
    }
}