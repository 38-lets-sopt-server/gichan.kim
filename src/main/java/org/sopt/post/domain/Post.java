package org.sopt.post.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.sopt.common.entity.BaseTimeEntity;
import org.sopt.user.domain.User;

@SQLDelete(sql = "UPDATE posts SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
@Entity
@Table(name = "posts")
public class Post extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;          // 게시글 상세 화면 — 특정 게시글 식별용

    private String title;     // 목록, 상세, 글쓰기 화면 — 제목

    private String content;   // 목록(미리보기), 상세(전체) 화면 — 내용

    @ManyToOne(fetch = FetchType.LAZY)  // User : Post = 1 : N
    @JoinColumn(name = "user_id")       // post 테이블에 user_id FK 컬럼이 생겨요
    private User user;

    BoardType boardType;    // 게시글 타입

    @Version
    private Long version;

    private boolean deleted = false;

    protected Post() {}

    private Post(String title, String content, BoardType boardType, User user) {
        this.title = title;
        this.content = content;
        this.boardType = boardType;
        this.user = user;
    }

    public static Post create(String title, String content, BoardType boardType, User user) {
        return new Post(title, content, boardType, user);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateToHot(){
        this.boardType = BoardType.HOT;
    }

    public Long getId() { return this.id; }
    public String getTitle() { return this.title; }
    public String getContent() { return this.content; }
    public BoardType getBoardType() {
        return this.boardType;
    }
    public User getUser() { return this.user; }
    public boolean isDeleted() { return deleted; }
}