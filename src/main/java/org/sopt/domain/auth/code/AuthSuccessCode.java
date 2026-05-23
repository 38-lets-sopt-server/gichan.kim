package org.sopt.domain.auth.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.global.common.code.BaseCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthSuccessCode implements BaseCode {
    REFRESH_TOKEN_REISSUE_SUCCESS(HttpStatus.OK, "토큰 리프레시에 성공했습니다."),
    GET_MY_INFO_SUCCESS(HttpStatus.OK, "내 정보 조회에 성공했습니다."),
    SIGN_UP_SUCCESS(HttpStatus.CREATED, "회원가입에 성공했습니다."),
    LOCAL_LOGIN_SUCCESS(HttpStatus.OK, "이메일 로그인에 성공했습니다."),
    KAKAO_LOGIN_SUCCESS(HttpStatus.OK, "카카오 로그인에 성공했습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공했습니다.");

    private final HttpStatus status;
    private final String message;

    public String getCode() { return name(); }
}
