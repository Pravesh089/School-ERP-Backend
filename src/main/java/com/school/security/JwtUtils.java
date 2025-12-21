package com.school.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

/**
 * Utility class for JWT (JSON Web Token) operations.
 * Handles token generation, validation, and parsing.
 */
@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationMs;

    /**
     * Generates a JWT token for the authenticated user.
     * 
     * @param authentication The Spring Security Authentication object containing user details.
     * @return A signed JWT string.
     */
    public String generateJwtToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Generates the cryptographic key from the secret string using HMAC-SHA.
     */
    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Extracts the username (subject) from a given JWT token.
     * 
     * @param token The JWT token string.
     * @return The username.
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Validates the JWT token signature and expiration.
     * 
     * @param authToken The JWT token string.
     * @return True if valid, false otherwise.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (JwtException e) {
            // Log error: e.g., SignatureException, MalformedJwtException, ExpiredJwtException, etc.
             System.err.println("Invalid JWT token: " + e.getMessage());
        }
        return false;
    }
}
