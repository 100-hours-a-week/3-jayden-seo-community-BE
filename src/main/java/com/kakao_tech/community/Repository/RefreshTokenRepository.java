package com.kakao_tech.community.Repository;

import com.kakao_tech.community.Entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
