package org.sopt.dto.response;

import org.sopt.domain.Post;

public class PostResponse {
    Long id;
    String title;
    String content;
    String author;
    String createdAt;

    private PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getAuthor();
        this.createdAt = post.getCreatedAt();
    }

    public static PostResponse from(Post post) {
        return new PostResponse(post);
    }

    @Override
    public String toString() {
        return "[" + id + "] " + title + " - " + author + " (" + createdAt + ")\n" + content;
    }
}
