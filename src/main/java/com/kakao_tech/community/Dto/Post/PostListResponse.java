package com.kakao_tech.community.Dto.Post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakao_tech.community.Entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostListResponse {
    private Long postId;
    private String title;
    private String authorName;
    private String authorProfileImage;

    private int likeCount;
    private int commentCount;
    private int viewCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    public static PostListResponse from(Post post) {
        return new PostListResponse(
                post.getId(),
                post.getTitle(),
                post.getMember().getNickname(),
                post.getMember().getProfileImageUrl(),
                post.getPostCounts().getLikeCount(),
                post.getPostCounts().getCommentCount(),
                post.getPostCounts().getViewCount(),
                post.getUpdatedAt()
        );
    }
}
