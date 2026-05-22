package org.sopt.domain.auth.dto.response;

import org.sopt.domain.auth.service.dto.LoginResult;
import org.sopt.domain.user.entity.User;

public record KakaoLoginResponse(
        Long id,
        String nickname,
        String email,
        String kakaoId
) {
    public static KakaoLoginResponse from(LoginResult result) {
        User user = result.user();
        return new KakaoLoginResponse(
                user.getId(), user.getNickname(), user.getEmail(), user.getProviderId()
        );
    }
}
