package com.lumina.luminabackend.dto.common;

import lombok.*;

/**
 * Generic API response wrapper for success or error results.
 * @param <T> type of the response data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO<T> {

    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return ApiResponseDTO.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponseDTO<T> error(String message) {
        return ApiResponseDTO.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }

    public static <T> ApiResponseDTO<T> error(String message, T data) {
        return ApiResponseDTO.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .build();
    }
}