package com.s2s;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    public static void verifySystemToken(String token, SecretKey systemKey) {
        try {
            Jws<Claims> claims = extractClaims(token, systemKey);
            if (claims == null) {
                throw new JwtException("Token is not signed by trusted services!");
            }
        } catch (Exception e) {
            throw new JwtException("Token is not signed by trusted services!");
        }
    }

    private static Jws<Claims> extractClaims(final String token, final SecretKey key) throws IOException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public static boolean isTokenExpired(final String token) {
        return extractClaimsWithoutVerification(token).getExpiration().before(new Date());
    }

    static Claims extractClaimsWithoutVerification(String token) {
        return Jwts.parserBuilder()
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String generateToken(final String systemName, final String uniqueUserId, final SecretKey secretKey) throws IOException {
        // 1 day
        Map<String, Object> claims = new HashMap<>();
        claims.put("system", systemName);
        long expirationTime = 86400000;
        return Jwts.builder()
                .setSubject(uniqueUserId)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    public static String extractUniqueUserId(String token, SecretKey secretKey) throws IOException {
        return extractClaims(token, secretKey).getBody().getSubject();
    }
}
