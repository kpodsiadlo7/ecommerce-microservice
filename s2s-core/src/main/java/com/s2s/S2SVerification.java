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

    public static boolean checkSystemIsRecognized(String system) {
        return !trustedServicesStore.containsKey(system);
    }

    public static SecretKey getSecretSystemKey(String systemName) {
        return trustedServicesStore.get(systemName);
    }

    public static boolean verifyRequest(String token) {
        SecretKey key = validTokenBeforeVerify(token);
        if (key != null) {
            JwtUtil.verifySystemToken(token, key);
            return true;
        }
        return false;
    }

    private static SecretKey validTokenBeforeVerify(String token) {
        String systemName = getSystemName(token);
        SecretKey key = trustedServicesStore.get(systemName);
        if (checkSystemIsRecognized(systemName)) {
            throw new IllegalArgumentException("Unknown service: " + systemName);
        }
        if (JwtUtil.isTokenExpired(token)) {
            throw new JwtException("Token is expired!");
        }
        return key;
    }

    private static String getSystemName(String token) {
        Claims claims = JwtUtil.extractClaimsWithoutVerification(token);
        return claims.get("system", String.class);
    }
}