package com.kakao_tech.community.Dto.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostListScrollResponse {
    private List<PostListResponse> posts;
    private boolean hasNext;
    private Long nextCursor;
}
