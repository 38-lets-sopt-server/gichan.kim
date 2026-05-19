package org.sopt.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.sopt.global.common.code.SuccessCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SuccessResponse<T>(
        boolean success,
        String code,
        String message,
        T data
) implements BaseResponse {
    public static SuccessResponse<Void> of(SuccessCode successCode) {
        return of(successCode, null);
    }

    public static <T> SuccessResponse<T> of(SuccessCode successCode, T data) {
        return new SuccessResponse<T>(
                true,
                successCode.getCode(),
                successCode.getMessage(),
                data
        );
    }
}
