package org.sopt.domain.auth.service.dto;

import org.sopt.domain.user.entity.User;

public record LoginResult(
        String accessToken,
        String refreshToken,
        User user) {
}
