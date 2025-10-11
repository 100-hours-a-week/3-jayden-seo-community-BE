package com.kakao_tech.community.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class PostCounts {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "post_id")
    private Post post;

    private int viewCount = 0;
    private int likeCount = 0;
    private int commentCount = 0;

    public PostCounts(Post post) {
        this.post = post;
    }
}
