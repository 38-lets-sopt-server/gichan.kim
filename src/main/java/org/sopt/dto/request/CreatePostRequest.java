package org.sopt.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.sopt.enums.BoardType;

public record CreatePostRequest(
        @NotBlank(message = "제목은 필수입니다!")
        @Size(max = 50, message = "제목은 50자를 초과할 수 없습니다.")
        String title,
        @NotBlank(message = "내용은 필수입니다!")
        @Size(max = 500, message = "내용은 500자를 초과할 수 없습니다.")
        String content,
        @NotNull
        BoardType boardType,
        Long userId
) {}
