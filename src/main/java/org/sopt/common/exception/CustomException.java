package org.sopt.common.exception;

public class CustomException extends RuntimeException {
    private final ResponseCode errorCode;

    public CustomException(ResponseCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ResponseCode getErrorCode() {
        return errorCode;
    }
}
