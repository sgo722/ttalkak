package com.ttalkak.project.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private int status;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "OK", 200, data);
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(true, null, 200, null);
    }

    public static <T> ApiResponse<T> fail(String message, int status) {
        return new ApiResponse<>(false, message, status, null);
    }

    public static <T> ApiResponse<Map<String, String>> fail(String message, Map<String, String> errors) {
        return new ApiResponse<>(false, message, 400, errors);
    }

    public static ApiResponse<Map<String, String>> fails(String message, int status, Map<String, String> errors) {
        return new ApiResponse<>(false, message, status, errors);
    }

    public static ApiResponse<Map<String, String>> fail(String message, int status, Map<String, String> errors) {
        return new ApiResponse<>(false, message, status, errors);
    }
}
