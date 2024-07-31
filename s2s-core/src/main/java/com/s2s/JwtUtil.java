package com.s2s;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtil {

    public static void verifySystemToken(String token, SecretKey systemKey) {
        try {
            Jws<Claims> claims = extractClaims(token, systemKey);
            if (claims == null) {
                log.warn("claims jest null");
                throw new JwtException("Token is not signed by trusted service!");
            }
        } catch (Exception e) {
            log.warn("Token nie jest podpisany przez zaufany system");
            throw new JwtException("Token is not signed by trusted service!");
        }
        log.info("Token jest ok");
    }

    private static Jws<Claims> extractClaims(final String token, final SecretKey key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public static boolean isTokenExpired(final String token) {
        log.info("token is expired?");
        return extractClaimsWithoutVerification(token).getExpiration().before(new Date());
    }

    static Claims extractClaimsWithoutVerification(String token) {
        JwtParser parser = Jwts.parserBuilder().build();
        String[] splitToken = token.split("\\.");
        String unsignedToken = splitToken[0] + "." + splitToken[1] + ".";
        return parser.parseClaimsJwt(unsignedToken).getBody();
    }

    public static String generateToken(final String systemName, final String uniqueUserId, final String role) throws IOException {
        SecretKey secretKey = S2SVerification.getSecretSystemKey(systemName);
        log.info("Generate new token");
        Map<String, Object> claims = new HashMap<>();
        claims.put("system", systemName);
        claims.put("sub", uniqueUserId);
        claims.put("role", role);

        // 1 day
        long expirationTime = 86400000;

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    public static JwtDetails extractJwtDetails(final String token, final SecretKey secretKey) {
        log.info("Extract unique user id and role from token");
        Jws<Claims> claimsJws = extractClaims(token, secretKey);
        return new JwtDetails(
                claimsJws.getBody().getSubject(),
                claimsJws.getBody().get("role").toString()
        );
    }
}
