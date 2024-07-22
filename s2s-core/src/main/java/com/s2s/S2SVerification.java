package com.s2s;

import java.util.HashMap;
import java.util.Map;

public class S2SVerification {

    private static final Map<String, String> trustedServices = new HashMap<>();

    public static void addToTrustedStore(final String appName, final String publicKey) {
        trustedServices.put(appName, publicKey);
    }

    public static boolean verifyRequest(String serviceName, String token) {
        String publicKey = trustedServices.get(serviceName);
        if (publicKey == null) {
            throw new IllegalArgumentException("Unknown service: " + serviceName);
        }
        return JwtUtil.verifyToken(token, publicKey);
    }
}