package com.likelion.springsession.post.service;

import com.likelion.springsession.post.dto.PostCreateRequest;
import com.likelion.springsession.post.dto.PostDetailResponse;
import com.likelion.springsession.post.dto.PostSummaryResponse;
import com.likelion.springsession.post.dto.PostUpdateRequest;
import com.likelion.springsession.post.entity.post;
import com.likelion.springsession.post.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<PostSummaryResponse> getPostsummaries(){
        List<post> posts = postRepository.findAll();
        List<PostSummaryResponse> responses = new ArrayList<>();

        for (post post : posts) {
            PostSummaryResponse response = new PostSummaryResponse(
                    post.getId(),
                    post.getTitle(),
                    post.getCreatedAt()
            );
            responses.add(response);
        }
        return responses;
    }

    public PostDetailResponse createPost(PostCreateRequest request){
        post post = new post(
                request.getTitle(),
                request.getContent()
        );

        post savedPost = postRepository.save(post);
        return toDetailResponse(savedPost);
    }
    private PostDetailResponse toDetailResponse(post post){
        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt()
        );
    }

    public PostDetailResponse getPost(Long postId){
        post post = findPostById(postId);
        return toDetailResponse(post);
    }

    private post findPostById(Long postId){
        return postRepository.findById(postId)
                .orElseThrow();
    }

    @Transactional
    public PostDetailResponse updatePost(
            Long postId,
            PostUpdateRequest request
    ){
        post post = findPostById(postId);

        post.update(
                request.getTitle(),
                request.getContent()
        );
        return toDetailResponse(post);
    }

    @Transactional
    public void deletePost(Long postId){
        post post = findPostById(postId);
        postRepository.delete(post);
    }
}
