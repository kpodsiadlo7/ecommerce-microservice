package com.s2s;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.Key;
import java.util.Date;

public class JwtUtil {

    public static boolean verifySystemToken(String token, SecretKey systemKey) {
        try {
            Jws<Claims> claims = extractClaims(token, systemKey);
            return claims != null;
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

    static boolean isTokenExpired(final String token) throws IOException {
        return extractClaimsWithoutVerification(token).getExpiration().before(new Date());
    }

    static Claims extractClaimsWithoutVerification(String token) {
        return Jwts.parserBuilder()
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
