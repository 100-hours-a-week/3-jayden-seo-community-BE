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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PostCountsRepository postCountsRepository;
    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentService commentService;

    private Post post;
    private Member member;
    private Comment comment;

    @BeforeEach
    void setup() {
        post = new Post();
        member = new Member();
        comment = new Comment("hello", post, member);
    }

    @Test
    @DisplayName("GET ALL COMMENTS")
    void getAllComments_ShouldReturnCommentList() {
        when(commentRepository.findByPostId(1L))
                .thenReturn(Arrays.asList(comment));

        List<CommentResponse> result = commentService.getAllComments(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getContent()).isEqualTo("hello");
        verify(commentRepository, times(1)).findByPostId(1L);
    }

    @Test
    @DisplayName("CREATE COMMENT")
    void createComment_ShouldCreateAndReturnCommentResponse() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(memberRepository.findById(2L)).thenReturn(Optional.of(member));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponse response = commentService.createComment(1L, 2L, "hello world");

        assertThat(response.getContent()).isEqualTo("hello");

        verify(commentRepository).save(any(Comment.class));
        verify(postCountsRepository).incrementCommentCount(1L);
    }

    @Test
    @DisplayName("CREATE COMMENT - PostNotFound Exception")
    void createComment_ShouldThrowException_WhenPostNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                commentService.createComment(1L, 2L, "test")
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("CREATE COMMENT - MemberNotFound Exception")
    void createComment_ShouldThrowException_WhenMemberNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(memberRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                commentService.createComment(1L, 2L, "test")
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("UPDATE COMMENT")
    void updateComment_ShouldUpdateContent() {
        // comment.validateAuthor(memberId) 를 통과시키기 위해 mocking 필요
        Long memberId = 10L;

        Comment spyComment = spy(new Comment("old", post, member));
        doNothing().when(spyComment).validateAuthor(memberId);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(spyComment));

        commentService.updateComment(1L, memberId, "new content");

        verify(spyComment).setContent("new content");
    }

    @Test
    @DisplayName("UPDATE COMMENT - CommentNotFound Exception")
    void updateComment_ShouldThrowException_WhenCommentNotFound() {
        when(commentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                commentService.updateComment(99L, 1L, "content")
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("DELETE COMMENT")
    void deleteComment_ShouldDeleteComment() {
        Long memberId = 3L;
        Comment spyComment = spy(comment);

        doNothing().when(spyComment).validateAuthor(memberId);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(spyComment));

        commentService.deleteComment(memberId, 1L);

        verify(postCountsRepository).decrementCommentCount(post.getId());
        verify(commentRepository).delete(spyComment);
    }

    @Test
    @DisplayName("DELETE COMMENT - CommentNotFound Exception")
    void deleteComment_ShouldThrowException_WhenCommentNotFound() {
        when(commentRepository.findById(111L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                commentService.deleteComment(1L, 111L)
        ).isInstanceOf(EntityNotFoundException.class);
    }
}
