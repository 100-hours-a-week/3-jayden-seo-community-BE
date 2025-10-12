package com.kakao_tech.community.Controller;

import com.kakao_tech.community.Dto.Member.MemberInfoResponse;
import com.kakao_tech.community.Dto.Member.RegisterDto;
import com.kakao_tech.community.Dto.Member.UpdatePasswordDto;
import com.kakao_tech.community.Dto.Member.UpdateProfileDto;
import com.kakao_tech.community.Service.MemberService;
import com.kakao_tech.community.Utils.LoginMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Map<String, Long>> addMember(
            @Valid @RequestBody RegisterDto registerDto) {

        Long memberId = memberService.addMember(registerDto);
        return ResponseEntity.ok().body(Map.of("memberId", memberId));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberInfoResponse> getMember(
            @LoginMember Long memberId) {

        MemberInfoResponse member = memberService.getMember(memberId);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<Void> updateMember(
            @Valid @RequestBody UpdateProfileDto updateProfileDto,
            @LoginMember Long memberId) {

        memberService.updateMember(memberId, updateProfileDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{memberId}/password")
    public ResponseEntity<Void> updateMemberPassword(
            @Valid @RequestBody UpdatePasswordDto updatePasswordDto,
            @LoginMember Long memberId) {

        memberService.updateMemberPassword(memberId, updatePasswordDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(
            @LoginMember Long memberId){

        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
