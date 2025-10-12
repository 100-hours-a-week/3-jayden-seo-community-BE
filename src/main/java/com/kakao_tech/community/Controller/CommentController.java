package com.kakao_tech.community.Controller;

import com.kakao_tech.community.Dto.Comment.CommentRequest;
import com.kakao_tech.community.Dto.Comment.CommentResponse;
import com.kakao_tech.community.Service.CommentService;
import com.kakao_tech.community.Utils.LoginMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<Map<String, List<CommentResponse>>> getComments(
            @PathVariable Long postId){
        List<CommentResponse> allComments = commentService.getAllComments(postId);
        return ResponseEntity.ok().body(Map.of("comments", allComments));
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest commentRequest,
            @LoginMember Long memberId){

        CommentResponse commentDto = commentService.createComment(postId, memberId, commentRequest.getContent());
        return ResponseEntity.ok().body(commentDto);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest commentRequest,
            @LoginMember Long memberId){

        commentService.updateComment(commentId, memberId, commentRequest.getContent());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @LoginMember Long memberId){

        commentService.deleteComment(memberId, commentId);
        return ResponseEntity.noContent().build();
    }
}
