package org.sopt.dto.response;

import org.sopt.domain.Post;

public record CreatePostResponse (
        Long id,
        String message
) {
    public static CreatePostResponse from(Post post) {
        return new CreatePostResponse(post.getId(),"게시글 등록 완료!");
    }
}

