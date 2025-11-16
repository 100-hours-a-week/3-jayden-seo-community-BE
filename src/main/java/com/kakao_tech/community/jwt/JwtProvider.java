package com.kakao_tech.community.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {

    private final Key key = Keys.hmacShaKeyFor(
            // kakaoCommunityforJayden.SeokakaoCommunityforJayden.Seo
            Base64.getDecoder().decode("a2FrYW9Db21tdW5pdHlmb3JKYXlkZW4uU2Vva2FrYW9Db21tdW5pdHlmb3JKYXlkZW4uU2Vv")
    );

    public String createAccessToken(Long memberId, String role){
        long accessTtlSec = 15 * 60;
        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(accessTtlSec)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String jwt){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
    }

    public String createRefreshToken(Long memberId){
        long refreshTtlSec = 14L * 24 * 3600; // 14일
        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .claim("typ", "refresh")
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(refreshTtlSec)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
