package org.sopt.common.exception;

import org.sopt.common.response.ApiResponse;
import org.sopt.common.response.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // CustomException 에러
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustom(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.fail(errorCode.getStatus().value(), errorCode.getMessage()));
    }

    // Validation 검증 에러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(
            MethodArgumentNotValidException e
    ) {
        String message = Objects.requireNonNull(e.getBindingResult()
                        .getFieldError())
                .getDefaultMessage();

        return ResponseEntity
                .status(org.springframework.http.HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(400, message));
    }

    // 예상치 못한 서버 내부 에러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {

        return ResponseEntity
                .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(500, "서버 오류"));
    }

    //Enum 변환 실패 및 잘못된 인자 전달 에러
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(400, "잘못된 요청 값입니다. 파라미터를 확인해주세요."));
    }
}
