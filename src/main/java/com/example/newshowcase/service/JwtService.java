package com.example.newshowcase.service;

import com.example.newshowcase.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateAccessToken(String userId, String login) {
        return Jwts.builder()
                .claims(Map.of("userId", userId, "login", login))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessExpiration()))
                .signWith(getAccessKey())
                .compact();
    }

    public Claims validateAccessToken(String token) {
        return Jwts.parser()
                .verifyWith(getAccessKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getAccessKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getAccessSecret().getBytes(StandardCharsets.UTF_8));
    }
}
