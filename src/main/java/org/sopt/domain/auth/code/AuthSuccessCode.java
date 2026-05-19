package org.sopt.domain.auth.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.global.common.code.BaseCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthSuccessCode implements BaseCode {
    TOKEN_ISSUE_SUCCESS(HttpStatus.OK, "토큰 발급에 성공했습니다."),
    GET_MY_INFO_SUCCESS(HttpStatus.OK, "내 정보 조회에 성공했습니다.");

    private final HttpStatus status;
    private final String message;

    public String getCode() { return name(); }
}
