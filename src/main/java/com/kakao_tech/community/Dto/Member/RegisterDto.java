package com.kakao_tech.community.Dto.Member;

import com.kakao_tech.community.Entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterDto {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 옳바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 최소 8자 최대 20자입니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 10, message = "닉네임은 최대 10글자입니다.")
    private String nickname;
    private String profileImageUrl;

    public Member toEntity(String encodedPassword){
        return new Member(email, nickname, encodedPassword, profileImageUrl);
    }
}
 