package com.likelion.springsession.comment.repository;

import com.likelion.springsession.comment.entity.Comment;
import com.likelion.springsession.post.entity.post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(post post);
    Optional<Comment> findByPostIdAndId(Long postId, Long id);
}
