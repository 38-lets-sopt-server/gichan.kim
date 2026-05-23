package org.sopt.domain.post.dto.request;

import jakarta.validation.constraints.NotNull;

// Spring Security 적용 이후 삭제 예정
public record DeletePostRequest(
        @NotNull
        Long userId
) {}
