package com.kakao_tech.community.Controller;


import com.kakao_tech.community.Dto.Login.LoginDto;
import com.kakao_tech.community.Dto.Login.LoginResponse;
import com.kakao_tech.community.Service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginDto loginDto,
            HttpServletRequest request) {

        Long memberId = authService.login(loginDto);
        request.getSession().setAttribute("memberId", memberId);

        return ResponseEntity.ok(new LoginResponse(memberId, "/posts"));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request){

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok("로그아웃 성공");
    }
}
