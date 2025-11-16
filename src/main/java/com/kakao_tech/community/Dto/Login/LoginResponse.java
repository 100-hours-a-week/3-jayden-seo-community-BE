package com.kakao_tech.community.Dto.Login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private Long memberId;
    private String profileImageUrl;
    private String accessToken;
    private String redirectUrl;
}
