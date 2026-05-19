package org.sopt.domain.post.dto.response;

import org.sopt.domain.post.entity.Post;

public record CreatePostResponse (
        Long id
) {
    public static CreatePostResponse from(Post post) {
        return new CreatePostResponse(post.getId());
    }
}