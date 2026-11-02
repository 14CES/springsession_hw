package com.likelion.springsession.comment.service;

import com.likelion.springsession.comment.dto.CommentCreateRequest;
import com.likelion.springsession.comment.dto.CommentResponse;
import com.likelion.springsession.comment.dto.CommentUpdateRequest;
import com.likelion.springsession.comment.entity.Comment;
import com.likelion.springsession.comment.exception.CommentAccessDeniedException;
import com.likelion.springsession.comment.repository.CommentRepository;
import com.likelion.springsession.post.dto.PostDetailResponse;
import com.likelion.springsession.post.entity.post;
import com.likelion.springsession.post.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;

import com.likelion.springsession.user.entity.User;
import com.likelion.springsession.user.exception.UserNotFoundException;
import com.likelion.springsession.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse addComment(Long userId, Long postId, CommentCreateRequest request) {

        post post = postRepository.findById(postId)
                .orElseThrow();

        User author = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Comment saved = commentRepository.save(new Comment(post, author, request.getContent()));

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
    public CommentResponse updateComment(Long userId, Long postId, Long commentId, CommentUpdateRequest request){
        Comment comment = findCommentByIdAndPostId(postId, commentId);

        if (!comment.isAuthor(userId)) {
            throw new CommentAccessDeniedException();
        }

        comment.update(request.getContent());
        return toResponse(comment);
    }

    @Transactional
    public void deleteComment(Long userId, Long postId, Long commentId){
        Comment comment = findCommentByIdAndPostId(postId, commentId);

        if (!comment.isAuthor(userId)) {
            throw new CommentAccessDeniedException();
        }

        comment.delete();
    }
}