package org.sopt.domain.like.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.global.common.code.BaseCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LikeSuccessCode implements BaseCode {
    LIKE_CREATED(HttpStatus.CREATED, "좋아요 추가 성공."),
    LIKE_DELETED(HttpStatus.OK, "좋아요 취소 성공.");

    private final HttpStatus status;
    private final String message;

    public String getCode() { return name(); }
}
