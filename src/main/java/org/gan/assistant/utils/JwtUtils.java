package org.gan.assistant.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;


    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    //生成Token
    public String generateToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSigningKey())
                .compact();
    }
    //从Token提取用户名
    public String extractUsername(String token){
        return getClaims(token).getSubject();
    }

    //验证Token
    public boolean validateToken(String token){
        try{
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
