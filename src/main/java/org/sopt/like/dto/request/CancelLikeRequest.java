package org.sopt.like.dto.request;

import jakarta.validation.constraints.NotNull;

public record CancelLikeRequest(
        @NotNull
        Long userId
) {}
