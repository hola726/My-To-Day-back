package com.jyp.service.my_today_service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey SECRET_KEY;
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

    @Autowired
    public JwtUtil() {
        try {
            // 파일에서 SECRET_KEY 읽기
            String secret = new String(Files.readAllBytes(Paths.get("src/main/java/com/jyp/service/my_today_service/config/jwt.secret.key"))).trim();
            if (secret.length() < 32) {
                throw new IllegalArgumentException("SECRET_KEY must be at least 32 characters long for HS256");
            }
            this.SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // SecretKey 생성
        } catch (Exception e) {
            throw new RuntimeException("Failed to load SECRET_KEY from file", e);
        }
    }

    // JWT 생성
    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY) // SecretKey 직접 사용
                .compact();
    }

    // JWT에서 사용자 이름 추출
    public String extractId(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // JWT 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
