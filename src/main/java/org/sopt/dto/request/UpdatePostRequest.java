package org.sopt.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePostRequest(
        @NotBlank(message = "제목은 필수입니다!")
        @Size(max = 50, message = "제목은 50자를 초과할 수 없습니다.")
        String newTitle,
        @NotBlank(message = "내용은 필수입니다!")
        @Size(max = 500, message = "내용은 500자를 초과할 수 없습니다.")
        String newContent
) {}
