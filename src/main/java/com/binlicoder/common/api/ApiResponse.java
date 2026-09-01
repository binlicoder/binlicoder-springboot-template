package com.binlicoder.common.api;

import java.time.Instant;

public record ApiResponse<T>(int code, String message, T data, Instant timestamp) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "操作成功", data, Instant.now());
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(200, "操作成功", null, Instant.now());
    }

    public static <T> ApiResponse<T> failure(int code, String message) {
        return new ApiResponse<>(code, message, null, Instant.now());
    }
}
