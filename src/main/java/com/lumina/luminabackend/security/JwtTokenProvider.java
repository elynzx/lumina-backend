package com.lumina.luminabackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Provides utility methods for generating, validating,
 * and extracting data from JWT tokens.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    /**
     * Generates and returns secret key used to sign JWT tokens.
     * @return SecretKey used for HMAC SHA-256/512 signing.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    /**
     * Generates JWT token based on user details.
     *
     * @param authentication current authenticated user
     * @return generated JWT token string
     */
    public String generateToken(Authentication authentication) {
        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        Date expiryDate = new Date(System.currentTimeMillis() + jwtProperties.getExpiration());

        return Jwts.builder()
                .subject(userPrincipal.getId().toString())
                .claim("email", userPrincipal.getUsername())
                .claim("role", userPrincipal.getRoleName())
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public Integer getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        return Integer.valueOf(claims.getSubject());
    }

    /**
     * Extracts username (email) from a JWT token.
     *
     * @param token JWT string
     * @return username inside the token
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("email", String.class);
    }

    /**
     * Extracts role from a JWT token.
     *
     * @param token JWT string
     * @return role inside the token
     */
    public String getRoleFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("role", String.class);
    }

    /**
     * Extracts al claims from a JWT token.
     *
     * @param token JWT string
     * @return claim objects inside the token
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Validates token and expiration date.
     *
     * @param authToken JWT string
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("Token JWT expirado: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Token JWT no soportado: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Token JWT malformado: {}", ex.getMessage());
        } catch (SecurityException ex) {
            log.error("Fallo en la firma del JWT: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string está vacío: {}", ex.getMessage());
        }
        return false;
    }
}
