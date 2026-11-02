package com.likelion.springsession.post.service;

import com.likelion.springsession.post.dto.PostCreateRequest;
import com.likelion.springsession.post.dto.PostDetailResponse;
import com.likelion.springsession.post.dto.PostSummaryResponse;
import com.likelion.springsession.post.dto.PostUpdateRequest;
import com.likelion.springsession.post.entity.post;
import com.likelion.springsession.post.exception.PostAccessDeniedException;
import com.likelion.springsession.post.repository.PostRepository;
import com.likelion.springsession.user.entity.User;
import com.likelion.springsession.user.exception.UserNotFoundException;
import com.likelion.springsession.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<PostSummaryResponse> getPostSummaries() {
        List<post> posts = postRepository.findAll();
        List<PostSummaryResponse> responses = new ArrayList<>();

        for (post post : posts) {
            PostSummaryResponse response = new PostSummaryResponse(
                    post.getId(),
                    post.getTitle(),
                    post.getAuthor().getNickname(),
                    post.getCreatedAt()
            );

            responses.add(response);
        }

        return responses;
    }

    // 게시글 조회 기능 추가
    public PostDetailResponse getPost(Long postId) {
        post post = findPostById(postId);
        return toDetailResponse(post);
    }

    // Service에 게시글 생성 기능 추가
    @Transactional
    public PostDetailResponse createPost(Long userId, PostCreateRequest request) {

        User author = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        post post = new post(
                author,
                request.getTitle(),
                request.getContent()
        );

        post savedPost = postRepository.save(post);

        return toDetailResponse(savedPost);
    }

    @Transactional
    public PostDetailResponse updatePost(
            Long userId,
            Long postId,
            PostUpdateRequest request
    ) {
        post post = findPostById(postId);

        if (!post.isAuthor(userId)) {
            throw new PostAccessDeniedException();
        }

        post.update(
                request.getTitle(),
                request.getContent()
        );

        return toDetailResponse(post);
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        post post = findPostById(postId);

        if (!post.isAuthor(userId)) {
            throw new PostAccessDeniedException();
        }

        postRepository.delete(post);
    }

    // 반복해서 사용할 조회 코드는 메서드로 분리
    private post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow();
    }

    // Entity를 Response DTO로 변환하기
    private PostDetailResponse toDetailResponse(post post) {
        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getNickname(),
                post.getCreatedAt()
        );
    }
}