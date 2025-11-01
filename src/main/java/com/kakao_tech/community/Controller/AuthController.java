package com.kakao_tech.community.Controller;


import com.kakao_tech.community.Dto.Login.LoginRequest;
import com.kakao_tech.community.Dto.Login.LoginResponse;
import com.kakao_tech.community.Service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/auth")
@RestController
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

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request){

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.noContent().build();
    }
}
