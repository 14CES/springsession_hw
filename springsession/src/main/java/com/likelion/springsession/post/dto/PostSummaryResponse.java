package com.likelion.springsession.post.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PostSummaryResponse {

    private final Long id;
    private final String title;
    private final String authorNickname;
    private final LocalDateTime createdAt;

}