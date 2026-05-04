package org.example.demo9090.keyloader;

import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyUtils {

    public static PrivateKey loadPrivateKey(String path) throws Exception {
        byte[] keyBytes = new ClassPathResource(path).getInputStream().readAllBytes();
        String content = new String(keyBytes, StandardCharsets.UTF_8);

        // Remove headers, footers, and all whitespace/newlines
        String cleanContent = content
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "");

        try {
            byte[] decoded = Base64.getDecoder().decode(cleanContent);
            return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid Base64 encoding in private key. Check for hidden characters.");
        }
    }

    public static PublicKey loadPublicKey(String path) throws Exception {
        byte[] keyBytes = new ClassPathResource(path).getInputStream().readAllBytes();
        String content = new String(keyBytes);

        String cleanContent = content
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(cleanContent);
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
    }
}


