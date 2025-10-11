package com.kakao_tech.community.Service;

import com.kakao_tech.community.Dto.Comment.CommentResponse;
import com.kakao_tech.community.Entity.Comment;
import com.kakao_tech.community.Entity.Member;
import com.kakao_tech.community.Entity.Post;
import com.kakao_tech.community.Repository.CommentRepository;
import com.kakao_tech.community.Repository.MemberRepository;
import com.kakao_tech.community.Repository.PostCountsRepository;
import com.kakao_tech.community.Repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostCountsRepository postCountsRepository;
    private final PostRepository postRepository;


    public List<CommentResponse> getAllComments(Long postId){
        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }

    public CommentResponse createComment(Long postId, Long memberId, String content){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Comment comment = new Comment(content, post, member);
        Comment savedComment = commentRepository.save(comment);
        postCountsRepository.incrementCommentCount(postId);

        return CommentResponse.from(savedComment);
    }

    @Transactional
    public void updateComment(Long memberId, Long commentId, String content){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        comment.validateAuthor(memberId);
        comment.setContent(content);
    }

    public void deleteComment(Long memberId, Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        comment.validateAuthor(memberId);
        postCountsRepository.decrementCommentCount(comment.getPost().getId());
        commentRepository.delete(comment);
    }
}
