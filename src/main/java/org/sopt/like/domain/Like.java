package org.sopt.like.domain;

import jakarta.persistence.*;
import org.sopt.common.entity.BaseTimeEntity;
import org.sopt.post.domain.Post;
import org.sopt.user.domain.User;

@Entity(name = "PostLike")
@Table(name = "likes")
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

    private Like(Post post, User user) {
        this.post = post;
        this.user = user;
    }

    public static Like create(Post post, User user) {
        return new Like(post, user);
    }

    public Long getId() { return id; }
    public Post getPost() { return post; }
    public User getUser() { return user; }
}
