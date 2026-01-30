package net.ayad.manageprojectbackend.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtility {

    private final static String secret = "rajamaroc2049EnsamCasablancaEstFesEstOujdaMasterBigData&IoT";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiration
                .signWith(secretKey)
                .compact();

    }

    public String extractEmail(String token) {
        return extractClaims(token)
                .getSubject();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token)
                .getExpiration()
                .before(new Date());
    }
}
