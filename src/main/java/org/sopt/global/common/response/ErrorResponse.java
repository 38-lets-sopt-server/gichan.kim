package org.sopt.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.sopt.global.common.code.BaseCode;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        boolean success,
        String code,
        String message,
        LocalDateTime timestamp,
        String path,
        List<FieldError> errors
) implements BaseResponse {

    public static ErrorResponse of(BaseCode errorCode, String path) {
        return of(errorCode, path, null);
    }

    public static ErrorResponse of(BaseCode errorCode, String path, List<FieldError> errors) {
        return new ErrorResponse(
                false,
                errorCode.getCode(),
                errorCode.getMessage(),
                LocalDateTime.now(),
                path,
                errors
        );
    }

    public record FieldError(
            String field,
            Object rejectedValue,
            String reason
    ) {}
}
