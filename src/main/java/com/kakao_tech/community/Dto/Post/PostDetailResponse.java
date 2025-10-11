package com.kakao_tech.community.Dto.Post;

import com.kakao_tech.community.Entity.Post;
import com.kakao_tech.community.Entity.PostImage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostDetailResponse {

    private Long postId;
    private String title;
    private String content;
    private String authorName;
    private String authorProfileImage;

    private int likeCount;
    private int commentCount;
    private int viewCount;

    private LocalDateTime updatedAt;
    private List<String> postImageUrls;

    public static PostDetailResponse from(Post post) {
        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getMember().getNickname(),
                post.getMember().getProfileImageUrl(),
                post.getPostCounts().getLikeCount(),
                post.getPostCounts().getCommentCount(),
                post.getPostCounts().getViewCount(),
                post.getUpdatedAt(),
                post.getPostImages().stream()
                        .map(PostImage::getImageUrl)
                        .toList()
        );
    }
}
