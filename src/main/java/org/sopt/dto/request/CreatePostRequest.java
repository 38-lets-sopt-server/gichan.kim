package org.sopt.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.sopt.enums.BoardType;

public record CreatePostRequest(
        @NotBlank(message = "제목은 필수입니다!")
        String title,
        @NotBlank(message = "내용은 필수입니다!")
        String content,
        @NotNull
        BoardType boardType,
        String author
) {}
