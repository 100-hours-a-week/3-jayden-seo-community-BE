package com.kakao_tech.community.Config.FIlter;

import com.kakao_tech.community.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private static final String[] EXCLUDE_PATH = {
            "/auth/login", "/member/register"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return Arrays.stream(EXCLUDE_PATH).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();
        String method = request.getMethod();

        // Preflight 요청 통과
        if ("OPTIONS".equalsIgnoreCase(method)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 로그인, 회원가입, 정적 리소스
        if (uri.startsWith("/auth/login")
                || uri.startsWith("/member/register")
                || uri.startsWith("/css")
                || uri.startsWith("/js")) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<String> token = extractToken(request);

        if(token.isEmpty()){
            unAuthorized(response);
            return;
        }

        if(!validateAndSetAttributes(token.get(), request)){
            unAuthorized(response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> extractToken(HttpServletRequest request) {
        return extractTokenFromHeader(request)
                .or(() -> extractTokenFromCookie(request));
    }

    private Optional<String> extractTokenFromHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7));
    }

    private Optional<String> extractTokenFromCookie(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> "accessToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    private boolean validateAndSetAttributes(String token, HttpServletRequest request) {
        try{
            var jws = jwtProvider.parse(token);
            Claims body = jws.getBody();
            request.setAttribute("memberId", Long.valueOf(body.getSubject()));
            log.info(body.get("role").toString());
            request.setAttribute("role", body.get("role"));
            return true;
        } catch(Exception ex) {
            return false;
        }
    }

    private void unAuthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");

        // 브라우저에서 차단되지 않도록 CORS 헤더 추가
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        response.getWriter().write("{\"message\": \"로그인이 필요합니다\"}");
    }
}
