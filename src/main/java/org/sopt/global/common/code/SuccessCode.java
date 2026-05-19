package org.sopt.global.common.code;

import org.springframework.http.HttpStatus;

public enum SuccessCode implements BaseCode {
    POST_CREATED(HttpStatus.CREATED, "게시글 생성 성공."),
    POST_LIST_FOUND(HttpStatus.OK, "게시글 목록 조회 성공."),
    POST_FOUND(HttpStatus.OK, "게시글 조회 성공."),
    POST_UPDATED(HttpStatus.OK, "게시글 수정 성공."),
    POST_DELETED(HttpStatus.OK, "게시글 삭제 성공."),
    LIKE_CREATED(HttpStatus.CREATED, "좋아요 추가 성공."),
    LIKE_DELETED(HttpStatus.OK, "좋아요 취소 성공."),
    TOKEN_ISSUE_SUCCESS(HttpStatus.OK, "토큰 발급에 성공했습니다."),
    GET_MY_INFO_SUCCESS(HttpStatus.OK, "내 정보 조회에 성공했습니다.");

    private final HttpStatus status;
    private final String message;

    SuccessCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }
    public String getCode() { return name(); }
}
