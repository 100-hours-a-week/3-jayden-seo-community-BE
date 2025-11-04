package com.kakao_tech.community.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter @Setter
@NoArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(nullable = false, unique = true, length = 100)
    private String nickname;

    @Column(nullable = false, length = 100)
    private String password;
    
    @Column(length = 255)
    private String profileImageUrl;

    public Member(String email, String nickname,
                  String password, String profileImageUrl) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
    }
}
