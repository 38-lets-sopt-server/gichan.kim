package org.sopt.domain.auth.dto.response;

import org.sopt.domain.auth.service.dto.LoginResult;
import org.sopt.domain.user.entity.User;

public record LoginResponse(
        Long id,
        String email,
        String nickname
) {
    public static LoginResponse from(LoginResult result) {
        User user = result.user();
        return new LoginResponse(
                user.getId(), user.getEmail(), user.getNickname()
        );
    }
}
