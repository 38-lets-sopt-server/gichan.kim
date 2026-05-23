package org.sopt.domain.post.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.global.common.code.BaseCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostSuccessCode implements BaseCode {
    POST_CREATED(HttpStatus.CREATED, "게시글 생성 성공."),
    POST_LIST_FOUND(HttpStatus.OK, "게시글 목록 조회 성공."),
    POST_FOUND(HttpStatus.OK, "게시글 조회 성공."),
    POST_UPDATED(HttpStatus.OK, "게시글 수정 성공."),
    POST_DELETED(HttpStatus.OK, "게시글 삭제 성공.");

    private final HttpStatus status;
    private final String message;

    public String getCode() { return name(); }
}
