package com.kakao_tech.community.Controller;


import com.kakao_tech.community.Dto.Login.LoginRequest;
import com.kakao_tech.community.Dto.Login.LoginResponse;
import com.kakao_tech.community.Exceptions.CustomExceptions.UnauthorizedException;
import com.kakao_tech.community.Service.AuthService;
import com.kakao_tech.community.Utils.LoginMember;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {

        LoginResponse loginResponse = authService.login(loginRequest, response);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(
            @CookieValue(value = "refreshToken", required = false) String refreshToken) {
        if(refreshToken == null) {
            throw new UnauthorizedException("Invalid refreshToken");
        }

        String newAccessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok().body(Map.of("accessToken", newAccessToken));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(
            @LoginMember Long memberId,
            HttpServletResponse response) {

        authService.logout(response, memberId);
        return ResponseEntity.noContent().build();
    }
}
