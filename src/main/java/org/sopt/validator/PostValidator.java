package org.sopt.validator;

import org.sopt.dto.request.CreatePostRequest;

public class PostValidator {

    public void validate(CreatePostRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다!");
        }
        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다!");
        }
    }
}