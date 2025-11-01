package com.kakao_tech.community.Service;

import com.kakao_tech.community.Dto.Login.LoginRequest;
import com.kakao_tech.community.Dto.Login.LoginResponse;
import com.kakao_tech.community.Entity.Member;
import com.kakao_tech.community.Entity.RefreshToken;
import com.kakao_tech.community.Exceptions.CustomExceptions.UnauthorizedException;
import com.kakao_tech.community.Repository.MemberRepository;
import com.kakao_tech.community.Repository.RefreshTokenRepository;
import com.kakao_tech.community.jwt.JwtProvider;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    private static final int ACCESS_TOKEN_EXPIRATION = 15 * 60; // 15분
    private static final int REFRESH_TOKEN_EXPIRATION = 14 * 24 * 3600; // 14일

    public LoginResponse login(LoginRequest loginRequest,
                               HttpServletResponse response) {

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));

        if(!passwordEncoder.matches(password, member.getPassword())){
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
        }

        TokenResponse tokenResponse = generateAndSaveTokens(member);
        addTokenCookie(response, "refreshToken", tokenResponse.refreshToken(), REFRESH_TOKEN_EXPIRATION);

        return new LoginResponse(member.getId(), member.getProfileImageUrl(),
                tokenResponse.accessToken(), "/posts");
    }

    private TokenResponse generateAndSaveTokens(Member member){
        String accessToken = jwtProvider.createAccessToken(member.getId(), member.getEmail());
        String refreshToken = jwtProvider.createRefreshToken(member.getId());

        RefreshToken refreshEntity = new RefreshToken();
        refreshEntity.setMemberId(member.getId());
        refreshEntity.setToken(refreshToken);
        refreshEntity.setExpiresAt(Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRATION));
        refreshEntity.setRevoked(false);
        refreshTokenRepository.save(refreshEntity);

        return new TokenResponse(accessToken, refreshToken);
    }

    private void addTokenCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }


    public record TokenResponse(String accessToken, String refreshToken) { }
}

