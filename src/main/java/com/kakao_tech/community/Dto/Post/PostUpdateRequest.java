package com.kakao_tech.community.Dto.Post;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostUpdateRequest {

    private String title;
    private String content;
    private List<String> imageUrls;
}
