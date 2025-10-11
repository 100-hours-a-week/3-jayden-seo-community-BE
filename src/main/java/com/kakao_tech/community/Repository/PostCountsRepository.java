package com.kakao_tech.community.Repository;

import com.kakao_tech.community.Entity.PostCounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCountsRepository extends JpaRepository<PostCounts, Long> {

    @Modifying
    @Query("UPDATE PostCounts pc SET pc.commentCount = pc.commentCount + 1 WHERE pc.post.id = :postId")
    void incrementCommentCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE PostCounts pc SET pc.commentCount = pc.commentCount - 1 WHERE pc.post.id = :postId")
    void decrementCommentCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE PostCounts pc SET pc.likeCount = pc.likeCount + 1 WHERE pc.post.id = :postId")
    void incrementLikeCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE PostCounts pc SET pc.likeCount = pc.likeCount - 1 WHERE pc.post.id = :postId")
    void decrementLikeCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE PostCounts pc SET pc.viewCount = pc.viewCount + 1 WHERE pc.post.id = :postId")
    void incrementViewCount(@Param("postId") Long postId);
}
