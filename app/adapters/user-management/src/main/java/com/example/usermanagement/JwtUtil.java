package com.example.usermanagement;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

@Slf4j
@Component
class JwtUtil {

    public String generateToken(String username) throws IOException {
        // 1 day
        long expirationTime = 86400000;
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, provideKey())
                .compact();
    }

    public String provideKey() throws IOException {
        return new String(Files.readAllBytes(Paths.get("key.txt")));
    }

    public String extractUsername(String token) throws IOException {
        return extractClaims(token).getSubject();
    }

    public Claims extractClaims(String token) throws IOException {
        return Jwts.parser()
                .setSigningKey(provideKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) throws IOException {
        return extractClaims(token).getExpiration().before(new Date());
    }
}