package org.sopt.domain.post.exception;

import org.sopt.global.common.code.BaseCode;
import org.springframework.http.HttpStatus;

public enum PostErrorCode implements BaseCode {
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    INVALID_BOARD_TYPE_VALUE(HttpStatus.BAD_REQUEST, "잘못된 게시판 타입 값입니다."),
    NOT_POST_OWNER(HttpStatus.FORBIDDEN, "해당 게시글의 변경 권한이 없습니다.");

    private final HttpStatus status;
    private final String message;

    PostErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }
    public String getCode() { return name(); }
}