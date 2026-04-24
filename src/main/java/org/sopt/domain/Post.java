package org.sopt.domain;

import org.sopt.dto.request.CreatePostRequest;
import org.sopt.enums.BoardType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Post {
    private Long id;          // 게시글 상세 화면 — 특정 게시글 식별용
    private String title;     // 목록, 상세, 글쓰기 화면 — 제목
    private String content;   // 목록(미리보기), 상세(전체) 화면 — 내용
    private String author;    // 목록, 상세 화면 — 글쓴이
    BoardType boardType;    // 게시글 타입
    private LocalDateTime createdAt; // 목록, 상세 화면 — 작성 시각

    private Post(Long id, String title, String content, String author, BoardType boardType, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.boardType = boardType;
        this.createdAt = createdAt;
    }

    public static Post createPost(Long id, CreatePostRequest request) {
        return new Post(id, request.title(), request.content(), request.author(), request.boardType(), LocalDateTime.now());
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateToHot(){
        this.boardType = BoardType.HOT;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthor() { return author; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public BoardType getBoardType() {
        return boardType;
    }
}
