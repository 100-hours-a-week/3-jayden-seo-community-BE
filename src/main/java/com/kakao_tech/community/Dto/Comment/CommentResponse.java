package com.kakao_tech.community.Dto.Comment;

import com.kakao_tech.community.Entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private Long commentId;
    private String content;
    private Long authorId;
    private String authorNickname;
    private String authorProfileImage;
    private LocalDateTime updatedAt;

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getMember().getId(),
                comment.getMember().getNickname(),
                comment.getMember().getProfileImageUrl(),
                comment.getUpdatedAt()
        );
    }
}
