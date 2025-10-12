package com.kakao_tech.community.Controller;

import com.kakao_tech.community.Dto.Member.MemberInfoResponse;
import com.kakao_tech.community.Dto.Member.RegisterRequest;
import com.kakao_tech.community.Dto.Member.UpdatePasswordRequest;
import com.kakao_tech.community.Dto.Member.UpdateProfileRequest;
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
            @Valid @RequestBody RegisterRequest registerRequest) {

        Long memberId = memberService.addMember(registerRequest);
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
            @Valid @RequestBody UpdateProfileRequest updateProfileRequest,
            @LoginMember Long memberId) {

        memberService.updateMember(memberId, updateProfileRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{memberId}/password")
    public ResponseEntity<Void> updateMemberPassword(
            @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest,
            @LoginMember Long memberId) {

        memberService.updateMemberPassword(memberId, updatePasswordRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(
            @LoginMember Long memberId){

        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
