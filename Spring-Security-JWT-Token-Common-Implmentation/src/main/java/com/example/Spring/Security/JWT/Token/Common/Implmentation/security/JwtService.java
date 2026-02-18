package com.example.Spring.Security.JWT.Token.Common.Implmentation.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private Key signingKey;

    /*
        Convert secret string to signing key
     */
    @PostConstruct
    public void init() {

        signingKey = Keys.hmacShaKeyFor(secret.getBytes());

    }


    /*
        Generate JWT token
     */
    public String generateToken(String username) {

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

    }

    /*
        Extract username from token
     */
    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);

    }

    /*
        Validate token
     */
    public boolean isTokenValid(String token, String username) {

        final String extractedUsername = extractUsername(token);

        return extractedUsername.equals(username) && !isTokenExpired(token);

    }

    /*
        Check expiration
     */
    private boolean isTokenExpired(String token) {

        return extractClaim(token, Claims::getExpiration).before(new Date());

    }

    /*
        Extract specific claim
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }

    /*
        Extract all claims
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith((SecretKey) signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

}
