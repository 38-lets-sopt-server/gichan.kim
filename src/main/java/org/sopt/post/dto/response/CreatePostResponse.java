package org.sopt.post.dto.response;

import org.sopt.post.domain.Post;

public record CreatePostResponse (
        Long id
) {
    public static CreatePostResponse from(Post post) {
        return new CreatePostResponse(post.getId());
    }
}