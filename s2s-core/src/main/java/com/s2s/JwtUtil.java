package com.s2s;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JwtUtil {

    public static boolean verifyToken(String token, String publicKey) {
        try {
            Key key = Keys.hmacShaKeyFor(publicKey.getBytes());
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return claims != null;
        } catch (Exception e) {
            return false;
        }
    }
}
