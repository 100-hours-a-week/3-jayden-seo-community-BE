package com.kakao_tech.community.Repository;

import com.kakao_tech.community.Entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p ORDER BY p.id DESC")
    List<Post> findFirstPage(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.id < :lastPostId ORDER BY p.id DESC")
    List<Post> findNextPage(@Param("lastPostId") Long lastPostId, Pageable pageable);
}
