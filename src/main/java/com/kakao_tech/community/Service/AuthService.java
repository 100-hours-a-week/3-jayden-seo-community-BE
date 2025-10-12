package com.kakao_tech.community.Service;

import com.kakao_tech.community.Dto.Login.LoginRequest;
import com.kakao_tech.community.Entity.Member;
import com.kakao_tech.community.Repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long login(LoginRequest loginRequest){

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));

        if(!passwordEncoder.matches(password, member.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return member.getId();
    }
}
