package com.s2s;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

public class S2SVerification {

    private static final Map<String, SecretKey> trustedServicesStore = new HashMap<>();

    public static void addToTrustedStore(final String appName, final SecretKey secretKey) {
        trustedServicesStore.put(appName, secretKey);
    }

    public static boolean checkSystem(String system){
        return trustedServicesStore.containsKey(system);
    }

    public static SecretKey getSecretSystemKey(String systemName){
        return trustedServicesStore.get(systemName);
    }

    public static void verifyRequest(String token) {
        Claims claims = JwtUtil.extractClaimsWithoutVerification(token);
        String serviceName = claims.get("system", String.class);
        SecretKey key = trustedServicesStore.get(serviceName);
        if (key == null) {
            throw new IllegalArgumentException("Unknown service: " + serviceName);
        }
        if (JwtUtil.isTokenExpired(token)) {
            throw new JwtException("Token is expired!");
        }
        JwtUtil.verifySystemToken(token, key);
    }
}