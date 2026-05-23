package org.sopt.global.jwt;

import org.springframework.http.ResponseCookie;

public class CookieUtil {

    // refresh token 생성
    public static ResponseCookie createRefreshToken(
            String value,
            long maxAge
    ) {
        return ResponseCookie.from("refreshToken", value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(maxAge)
                .sameSite("None")
                .build();
    }

    // refresh token 삭제
    public static ResponseCookie deleteRefreshToken() {
        return ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();
    }
}
