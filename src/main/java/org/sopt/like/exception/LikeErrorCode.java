package org.sopt.like.exception;

import org.sopt.common.exception.ResponseCode;
import org.springframework.http.HttpStatus;

public enum LikeErrorCode implements ResponseCode {
    ALREADY_LIKED(HttpStatus.CONFLICT, "이미 좋아요를 누른 게시글입니다."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요 기록을 찾을 수 없습니다."),
    LIKE_CONFLICT_RETRY_FAILED(HttpStatus.CONFLICT, "좋아요 처리 중 충돌이 발생했습니다. 잠시 후 다시 시도해주세요.");

    private final HttpStatus status;
    private final String message;

    LikeErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }
    public String getCode() { return name(); }
}
