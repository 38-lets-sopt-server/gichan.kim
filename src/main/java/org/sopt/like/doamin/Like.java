package org.sopt.like.doamin;

import jakarta.persistence.*;
import org.sopt.common.entity.BaseTimeEntity;
import org.sopt.post.domain.Post;
import org.sopt.user.domain.User;

@Entity
@Table(name = "likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "post_id"})
})
public class Like extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    protected Like() {}

    private Like(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    public static Like create(Post post, User user) {
        return new Like(user, post);
    }

    public Long getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public User getUser() {
        return user;
    }
}
