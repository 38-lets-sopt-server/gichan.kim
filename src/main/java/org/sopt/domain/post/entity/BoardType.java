package org.sopt.domain.post.entity;

public enum BoardType {
    FREE("자유 게시판"),
    HOT("HOT 게시판"),
    SECRET("비밀 게시판");

    private final String description;

    BoardType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}