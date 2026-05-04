package org.example.demo9090.service;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.example.demo9090.keyloader.KeyUtils;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

@Service
public class JwtService {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            // Ensure these files are in src/main/resources
            this.privateKey = KeyUtils.loadPrivateKey("private_key.pem");
            this.publicKey = KeyUtils.loadPublicKey("public.pem");
            System.out.printf("");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load JWT keys from resources", e);
        }
    }

    /**
     * Generates a token using the RSA Private Key
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .signWith(privateKey) // Algorithm (RS256) is auto-detected from the key type
                .compact();
    }

    /**
     * Validates the token using the RSA Public Key
     */
    public String validateTokenAndGetSubject(String token) {
        return Jwts.parser()
                .verifyWith(publicKey) // New 0.12.x method
                .build()
                .parseSignedClaims(token) // New 0.12.x method
                .getPayload() // Replaces getBody()
                .getSubject();
    }
}


