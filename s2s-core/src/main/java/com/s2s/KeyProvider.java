package com.s2s;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class KeyProvider {

    public static SecretKey provideKey(String path) throws IOException {
        String keyString = new String(Files.readAllBytes(Paths.get(path)));
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        return Keys.hmacShaKeyFor(decodedKey);
    }
}
