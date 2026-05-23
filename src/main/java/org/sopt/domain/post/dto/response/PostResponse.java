package org.sopt.domain.post.dto.response;

import org.sopt.domain.post.entity.Post;
import org.sopt.domain.post.entity.BoardType;

import java.time.LocalDateTime;

public record PostResponse(Long id, String title, String content, BoardType boardType, LocalDateTime createdAt) {

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getBoardType(),
                post.getCreatedAt()
        );
    }
}
