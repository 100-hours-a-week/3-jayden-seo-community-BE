package com.kakao_tech.community.Repository;

import com.kakao_tech.community.Entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    void deleteByPostId(Long postId);
}
