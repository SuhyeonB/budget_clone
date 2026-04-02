package com.example.budget_clone.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j(topic = "JwtUtil")
public class JwtUtil {

    private static final long ACCESS_TOKEN_TIME = 5 * 60 * 1000L;
    private static final long REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000L;

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createAccessToken(Long userId, String email) {
        return Jwts.builder()
                .claim("userId", userId)
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_TIME))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long userId, String email) {
        return Jwts.builder()
                .claim("userId", userId)
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_TIME))
                .signWith(key)
                .compact();
    }
    public boolean validateToken(String token) {
        try {
            extranctClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.debug("Invalid JWT signature/token", e);
        } catch (ExpiredJwtException e) {
            log.debug("Expired JWT token", e);
        } catch (UnsupportedJwtException e) {
            log.debug("Unsupported JWT token", e);
        } catch (IllegalArgumentException e) {
            log.debug("JWT claims string is empty", e);
        }
        return false;
    }

    public Claims extranctClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getEmail(String token) {
        return extranctClaims(token).getSubject();
    }

    public Long getUserId(String token) {
        return extranctClaims(token).get("userId", Long.class);
    }
}
