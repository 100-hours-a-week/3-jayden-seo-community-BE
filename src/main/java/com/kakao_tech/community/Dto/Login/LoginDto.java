package com.kakao_tech.community.Dto.Login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDto {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 옳바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 최소 8자 최대 20자입니다.")
    private String password;
}
