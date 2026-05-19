package org.sopt.domain.auth.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.global.common.code.BaseCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements BaseCode {
    EMPTY_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN_SUBJECT(HttpStatus.UNAUTHORIZED, "토큰의 회원 정보가 올바르지 않습니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public String getCode() { return name(); }
}
