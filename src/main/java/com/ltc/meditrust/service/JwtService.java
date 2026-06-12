package com.ltc.meditrust.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.Key;


@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    public String extractEmail(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isValid(String token) {

        try {

            System.out.println(
                    "VALIDATING TOKEN..."
            );

            Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token);

            System.out.println(
                    "TOKEN IS VALID"
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "TOKEN IS INVALID"
            );

            e.printStackTrace();

            return false;
        }
    }

    private Key getSignInKey() {

        return Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );
    }
}