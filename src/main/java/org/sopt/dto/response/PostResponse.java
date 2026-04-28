package org.sopt.dto.response;

import org.sopt.domain.Post;
import org.sopt.enums.BoardType;

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
