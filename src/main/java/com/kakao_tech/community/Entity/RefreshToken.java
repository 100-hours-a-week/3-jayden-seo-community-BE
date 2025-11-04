package com.kakao_tech.community.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter @Setter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @Column(unique = true, length = 255, nullable = false)
    private String token;
    @Column(nullable = false)
    private Instant expiresAt;
    private boolean revoked = false;
}
