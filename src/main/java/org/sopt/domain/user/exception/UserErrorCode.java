package org.sopt.domain.user.exception;

import org.sopt.global.common.code.BaseCode;
import org.springframework.http.HttpStatus;

public enum UserErrorCode implements BaseCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    UserErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }
    public String getCode() { return name(); }
}