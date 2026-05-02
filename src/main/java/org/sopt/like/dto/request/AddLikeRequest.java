package org.sopt.like.dto.request;

import jakarta.validation.constraints.NotNull;

public record AddLikeRequest (
        @NotNull
        Long userId
) {}
