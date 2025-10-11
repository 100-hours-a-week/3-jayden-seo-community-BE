package com.kakao_tech.community.Controller;

import com.kakao_tech.community.Dto.Post.*;
import com.kakao_tech.community.Service.PostService;
import com.kakao_tech.community.Utils.LoginMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<PostListScrollResponse> getPosts(
            @RequestParam(value = "lastId", required = false) Long lastId){
        PostListScrollResponse postResponse = postService.getPosts(lastId);
        return ResponseEntity.ok().body(postResponse);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPost(
            @PathVariable Long postId){
        PostDetailResponse post = postService.getPostById(postId);
        return ResponseEntity.ok().body(post);
    }

    @PostMapping
    public ResponseEntity<Map<String, Long>> createPost(
            @Valid @RequestBody PostCreateRequest postCreateRequest,
            @LoginMember Long memberId){
//        Long memberId = (Long)request.getSession(true).getAttribute("memberId");
        Long postId = postService.createPost(memberId, postCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("postId", postId));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest updateRequest,
            @LoginMember Long memberId){
//        Long memberId = (Long)request.getSession(true).getAttribute("memberId");

        postService.updatePost(memberId, postId, updateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @LoginMember Long memberId){
//        Long memberId = (Long)request.getSession(true).getAttribute("memberId");
        postService.deletePost(memberId, postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> likePost(
            @PathVariable Long postId,
            @LoginMember Long memberId){
//        Long memberId = (Long)request.getSession(true).getAttribute("memberId");
        postService.likePost(memberId, postId);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<Void> unlikePost(
            @PathVariable Long postId,
            @LoginMember Long memberId){
//        Long memberId = (Long)request.getSession(true).getAttribute("memberId");
        postService.unlikePost(memberId, postId);
        return ResponseEntity.noContent().build();
    }
}
