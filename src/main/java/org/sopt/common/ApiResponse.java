package org.sopt.common;

public class ApiResponse<T> {

    private int status;
    private String message;
    private T data;

    public ApiResponse() {}

    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() { return status; }

    public String getMessage() { return message; }

    public T getData() { return data; }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>(200, message, data);
    }

    public static ApiResponse<Void> ok(String message) {
        return new ApiResponse<>(200, message, null);
    }

    public static <T> ApiResponse<T> fail(int status, String message) {
        return new ApiResponse<>(status, message, null);
    }
}
