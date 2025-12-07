package com.kakao_tech.community.Service;

import com.kakao_tech.community.Dto.Member.RegisterRequest;
import com.kakao_tech.community.Dto.Member.UpdatePasswordRequest;
import com.kakao_tech.community.Entity.Member;
import com.kakao_tech.community.Exceptions.CustomExceptions.DuplicateException;
import com.kakao_tech.community.Exceptions.CustomExceptions.PasswordMismatchException;
import com.kakao_tech.community.Repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("Member Create Test")
    public void createMemberTest() {
        RegisterRequest request = new RegisterRequest(
                "test@email.com",
                "1234",
                "1234",
                "jayden",
                "image@url.com");

        String email = request.getEmail();
        String nickname = request.getNickname();
        String encodePassword = "encodedPassword:1234";

        Member savedMember = request.toEntity(encodePassword);
        savedMember.setId(1L);

        //given
        when(memberRepository.existsByEmail(email)).thenReturn(false);
        when(memberRepository.existsByNickname(nickname)).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodePassword);
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);


        //when
        Long id = memberService.addMember(request);

        //then
        assertThat(id).isEqualTo(savedMember.getId());
        assertThat(1d).isEqualTo(1L);
    }

    @Test
    @DisplayName("Member Create Test - 존재하는 Email")
    public void createMemberFailTest() {
        RegisterRequest request = new RegisterRequest(
                "test@email.com",
                "1234",
                "1234",
                "jayden",
                "image@url.com");

        when(memberRepository.existsByEmail(request.getEmail())).thenReturn(true);
        assertThrows(DuplicateException.class, ()
            -> memberService.addMember(request));
    }


    @Test
    @DisplayName("Member Password Update Test")
    public void updateMemberTest() {
        Long memberId = 1L;
        Member member = new Member();
        member.setPassword("encodedPassword:1234");
        UpdatePasswordRequest request = new UpdatePasswordRequest("12345", "12345");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword:12345");

        memberService.updateMemberPassword(memberId, request);
        assertThat(member.getPassword()).isEqualTo("encodedPassword:12345");
    }
}