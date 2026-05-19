package org.sopt.global.exception;

import org.sopt.global.common.code.BaseCode;

public class CustomException extends RuntimeException {
    private final BaseCode errorCode;

    public CustomException(BaseCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BaseCode getErrorCode() {
        return errorCode;
    }
}
