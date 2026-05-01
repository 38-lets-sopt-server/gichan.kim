package org.sopt.post.dto.response;

import org.sopt.post.domain.Post;
import org.sopt.post.domain.BoardType;

import java.time.LocalDateTime;

public record PostListResponse(Long id, String nickname, String title, String content, BoardType boardType, Long likeCount,LocalDateTime createdAt) {

    public static PostListResponse from(Post post, String nickname, Long likeCount) {
        return new PostListResponse(
                post.getId(),
                nickname,
                post.getTitle(),
                post.getContent(),
                post.getBoardType(),
                likeCount,
                post.getCreatedAt()
        );
    }
}