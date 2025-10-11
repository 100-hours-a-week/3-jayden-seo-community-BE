package com.kakao_tech.community.Dto.Member;

import com.kakao_tech.community.Entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberInfoResponse {

    private String email;
    private String nickname;
    private String imageUrl;

    public static MemberInfoResponse from(Member member){
        return new MemberInfoResponse(
                member.getEmail(),
                member.getNickname(),
                member.getProfileImageUrl()
        );
    }
}
