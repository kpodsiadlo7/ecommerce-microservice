package com.s2s;

import io.jsonwebtoken.*;

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
                System.out.println("claims jest null");
                throw new JwtException("Token is not signed by trusted service!");
            }
        } catch (Exception e) {
            System.out.println("nie jest podpisany przez zaufany system");
            throw new JwtException("Token is not signed by trusted service!");
        }
    }

    private static Jws<Claims> extractClaims(final String token, final SecretKey key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public static boolean isTokenExpired(final String token) {
        System.out.println("token is expired? " + extractClaimsWithoutVerification(token).getExpiration().before(new Date()));
        return extractClaimsWithoutVerification(token).getExpiration().before(new Date());
    }

    static Claims extractClaimsWithoutVerification(String token) {
        JwtParser parser = Jwts.parserBuilder().build();
        String[] splitToken = token.split("\\.");
        String unsignedToken = splitToken[0] + "." + splitToken[1] + ".";
        return parser.parseClaimsJwt(unsignedToken).getBody();
    }

    public static String generateToken(final String systemName, final String uniqueUserId, final SecretKey secretKey, final String role) throws IOException {
        System.out.println("generate new token");
        // 1 day
        Map<String, Object> claims = new HashMap<>();
        claims.put("system", systemName);
        claims.put("sub", uniqueUserId);
        claims.put("role", role);
        long expirationTime = 86400000;
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    public static JwtDetails extractJwtDetails(final String token, final SecretKey secretKey) {
        System.out.println("extract unique user id");
        Jws<Claims> claimsJws = extractClaims(token, secretKey);
        System.out.println(claimsJws);
        return new JwtDetails(
                claimsJws.getBody().getSubject(),
                claimsJws.getBody().get("role").toString()
        );
    }
}
