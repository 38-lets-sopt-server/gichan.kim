package org.sopt.domain.auth.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.global.common.code.BaseCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements BaseCode {
    EMPTY_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "엑세스 토큰이 없습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 엑세스 토큰입니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 엑시스 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    INVALID_ACCESS_TOKEN_SUBJECT(HttpStatus.UNAUTHORIZED, "토큰의 회원 정보가 올바르지 않습니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "사용자 인증에 실패하였습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 리프레시 토큰입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),

    // 카카오 OAuth
    KAKAO_TOKEN_REQUEST_FAILED(HttpStatus.BAD_GATEWAY, "카카오 토큰 요청에 실패했습니다."),
    KAKAO_USER_INFO_REQUEST_FAILED(HttpStatus.BAD_GATEWAY, "카카오 사용자 정보 조회에 실패했습니다."),
    KAKAO_INVALID_RESPONSE(HttpStatus.BAD_GATEWAY, "카카오 응답을 처리할 수 없습니다."),
    INVALID_AUTHORIZATION_CODE(HttpStatus.BAD_REQUEST, "유효하지 않은 인가 코드입니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public String getCode() { return name(); }
}
