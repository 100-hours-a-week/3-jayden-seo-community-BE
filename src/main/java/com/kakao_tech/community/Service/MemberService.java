package com.kakao_tech.community.Service;

import com.kakao_tech.community.Dto.Member.MemberInfoResponse;
import com.kakao_tech.community.Dto.Member.RegisterRequest;
import com.kakao_tech.community.Dto.Member.UpdatePasswordRequest;
import com.kakao_tech.community.Dto.Member.UpdateProfileRequest;
import com.kakao_tech.community.Entity.Member;
import com.kakao_tech.community.Exceptions.CustomExceptions.PasswordMismatchException;
import com.kakao_tech.community.Repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long addMember(RegisterRequest dto) {

        String password = dto.getPassword();
        String passwordConfirm = dto.getPasswordConfirm();
        if(!password.equals(passwordConfirm)) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }

        validateDuplicateMemberEmail(dto.getEmail());
        validateDuplicateMemberNickname(dto.getNickname());
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Member member = dto.toEntity(encodedPassword);
        return memberRepository.save(member).getId();
    }

    public MemberInfoResponse getMember(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        return MemberInfoResponse.from(member);
    }

    @Transactional
    public void updateMember(Long memberId, UpdateProfileRequest updateProfileRequest) {
        String nickname = updateProfileRequest.getNickname();
        String imageUrl = updateProfileRequest.getProfileImageUrl();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        if(!member.getNickname().equals(nickname)) {
            validateDuplicateMemberNickname(nickname);
            member.setNickname(nickname);
        }

        if(imageUrl != null){
            member.setProfileImageUrl(imageUrl);
        }
    }

    @Transactional
    public void updateMemberPassword(Long memberId, UpdatePasswordRequest updatePasswordRequest) {
        String password = updatePasswordRequest.getPassword();
        String confirmPassword = updatePasswordRequest.getPasswordConfirm();

        if(!password.equals(confirmPassword)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        member.setPassword(passwordEncoder.encode(password));
    }

    public void deleteMember(Long memberId){
        memberRepository.deleteById(memberId);
    }


    private void validateDuplicateMemberEmail(String email){
        if(memberRepository.existsByEmail(email)){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
    }

    public void validateDuplicateMemberNickname(String nickname){
        if(memberRepository.existsByNickname(nickname)){
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
    }
}
