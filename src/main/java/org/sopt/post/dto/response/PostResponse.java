package org.sopt.post.dto.response;

import org.sopt.post.domain.Post;
import org.sopt.post.domain.BoardType;

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
