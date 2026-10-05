package com.likelion.springsession.post.repository;

import com.likelion.springsession.post.entity.post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<post, Long> {
}
