package org.sopt.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.sopt.global.common.code.BaseCode;
import org.sopt.global.common.code.GlobalErrorCode;
import org.sopt.global.common.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // CustomException 에러
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustom(
            CustomException e,
            HttpServletRequest request) {
        BaseCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, request.getRequestURI()));
    }

    // Validation 검증 에러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        List<ErrorResponse.FieldError> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ErrorResponse.FieldError(
                        err.getField(),
                        err.getRejectedValue(),
                        err.getDefaultMessage()))
                .toList();

        return ResponseEntity
                .status(GlobalErrorCode.INVALID_INPUT_VALUE.getStatus())
                .body(ErrorResponse.of(GlobalErrorCode.INVALID_INPUT_VALUE, request.getRequestURI(), errors));
    }

    // 예상치 못한 서버 내부 에러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception e,
            HttpServletRequest request) {

        return ResponseEntity
                .status(GlobalErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ErrorResponse.of(GlobalErrorCode.INTERNAL_SERVER_ERROR, request.getRequestURI()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException e,
            HttpServletRequest request) {

        Throwable cause = e.getCause();
        while (cause != null) {
            if (cause instanceof CustomException customException) {
                return ResponseEntity
                        .status(customException.getErrorCode().getStatus())
                        .body(ErrorResponse.of(customException.getErrorCode(), request.getRequestURI()));
            }
            cause = cause.getCause();
        }
            return ResponseEntity
                    .status(GlobalErrorCode.INVALID_REQUEST.getStatus())
                    .body(ErrorResponse.of(GlobalErrorCode.INVALID_REQUEST, request.getRequestURI()));

    }
}
