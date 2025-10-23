package com.kakao_tech.community.Service;

import com.kakao_tech.community.Dto.Post.*;
import com.kakao_tech.community.Entity.*;
import com.kakao_tech.community.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository imageRepository;
    private final PostCountsRepository countsRepository;
    private final PostLikeRepository postLikeRepository;
    private final MemberRepository memberRepository;
    private static final int SIZE = 10;

    public PostListScrollResponse getPosts(Long lastId){
        Pageable pageable = PageRequest.of(0, SIZE);

        List<Post> posts = (lastId == null) ? postRepository.findFirstPage(pageable)
                : postRepository.findNextPage(lastId, pageable);

        boolean hasNext = posts.size() == SIZE;
        Long nextCursor = hasNext ? posts.getLast().getId() : null;

        List<PostListResponse> postResponse = posts.stream()
                .map(PostListResponse::from)
                .toList();

        return new PostListScrollResponse(postResponse, hasNext, nextCursor);
    }

    @Transactional
    public PostDetailResponse getPostById(Long postId, Long memberId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        boolean isLiked = postLikeRepository.existsByMemberIdAndPostId(memberId, postId);
        countsRepository.incrementViewCount(postId);
        return PostDetailResponse.from(post, isLiked);
    }

    public Long createPost(Long memberId, PostCreateRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        String title = request.getTitle();
        String content = request.getContent();

        Post post = new Post(title, content, member);
        Post savedPost = postRepository.save(post);

        request.getImageUrls().forEach(imageUrl -> {
            PostImage postImage = new PostImage(imageUrl);
            postImage.setPost(savedPost);
            imageRepository.save(postImage);
        });

        PostCounts postCounts = new PostCounts(savedPost);
        countsRepository.save(postCounts);

        return savedPost.getId();
    }

    @Transactional
    public void updatePost(Long memberId, Long postId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        post.validateAuthor(memberId);

        if(request.getTitle() != null){
            post.setTitle(request.getTitle());
        }
        if(request.getContent() != null){
            post.setContent(request.getContent());
        }
        if(request.getImageUrls() != null){
            imageRepository.deleteByPostId(postId);

            request.getImageUrls().forEach(url -> {
                PostImage postImage = new PostImage(url);
                postImage.setPost(post);
                imageRepository.save(postImage);
            });
        }

    }
    public void deletePost(Long memberId, Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        post.validateAuthor(memberId);
        postRepository.delete(post);
    }

    @Transactional
    public void likePost(Long memberId, Long postId){

        if(postLikeRepository.existsByMemberIdAndPostId(memberId, postId)){
            throw new IllegalStateException("이미 좋아요를 한 상태입니다.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        PostLike postLike = new PostLike(member, post);
        postLikeRepository.save(postLike);
        countsRepository.incrementLikeCount(postId);
    }

    @Transactional
    public void unlikePost(Long memberId, Long postId){
        PostLike postLike = postLikeRepository.findByMemberIdAndPostId(memberId, postId)
                .orElseThrow(() -> new EntityNotFoundException("Post like not found"));
        postLikeRepository.delete(postLike);
        countsRepository.decrementLikeCount(postId);
    }
}
