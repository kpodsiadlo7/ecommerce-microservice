package com.s2s;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class S2SVerification {

    private static final Map<String, SecretKey> trustedServicesStore = new HashMap<>();

    public static void addToTrustedStore(final String appName, final SecretKey key) {
        trustedServicesStore.put(appName, key);
    }

    public static boolean verifyRequest(String token) throws IOException {
        Claims claims = JwtUtil.extractClaimsWithoutVerification(token);
        String serviceName = claims.get("system", String.class);
        SecretKey key = trustedServicesStore.get(serviceName);
        if (key == null) {
            throw new IllegalArgumentException("Unknown service: " + serviceName);
        }
        if (JwtUtil.isTokenExpired(token)) {
            throw new JwtException("Token is expired!");
        }
        return JwtUtil.verifySystemToken(token, key);
    }
}