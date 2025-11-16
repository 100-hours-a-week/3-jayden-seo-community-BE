package com.kakao_tech.community.Entity;

import com.kakao_tech.community.Exceptions.CustomExceptions.ForbiddenException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter @Setter
@NoArgsConstructor
public class Comment extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Comment(String content, Post post, Member member) {
        this.content = content;
        this.post = post;
        this.member = member;
    }

    public boolean isAuthor(Long memberId) {
        return this.member.getId().equals(memberId);
    }

    public void validateAuthor(Long memberId) {
        if (!isAuthor(memberId)) {
            throw new ForbiddenException("권한이 없습니다.");
        }
    }
}