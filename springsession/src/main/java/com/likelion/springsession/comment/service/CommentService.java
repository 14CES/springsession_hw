package com.likelion.springsession.comment.service;

import com.likelion.springsession.comment.dto.CommentCreateRequest;
import com.likelion.springsession.comment.dto.CommentResponse;
import com.likelion.springsession.comment.dto.CommentUpdateRequest;
import com.likelion.springsession.comment.entity.Comment;
import com.likelion.springsession.comment.repository.CommentRepository;
import com.likelion.springsession.post.dto.PostDetailResponse;
import com.likelion.springsession.post.entity.post;
import com.likelion.springsession.post.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponse addComment(Long postId, CommentCreateRequest request){
        post post = postRepository.findById(postId)
                .orElseThrow();

        Comment saved = commentRepository.save(new Comment(post, request.getContent()));
        return new CommentResponse(saved);
    }

    public List<CommentResponse> getComments(Long postId){
        post post = postRepository.findById(postId)
                .orElseThrow();

        List<CommentResponse> responses = new ArrayList<>();
        for (Comment comment : commentRepository.findAllByPost(post)){
            responses.add(new CommentResponse(comment));
        }
        return responses;
    }

    private Comment findCommentByIdAndPostId(Long postId, Long commentId) {
        return commentRepository.findByPostIdAndId(postId, commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다"));
    }

    private CommentResponse toResponse(Comment comment) {
        return new CommentResponse(comment);
    }

    @Transactional
    public CommentResponse updateComment(Long postId, Long commentId, CommentUpdateRequest request){
        Comment comment = findCommentByIdAndPostId(postId, commentId);
        comment.update(request.getContent());
        return toResponse(comment);
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId){
        Comment comment = findCommentByIdAndPostId(postId, commentId);
        comment.delete();
    }
}