package com.ttalkak.user.user.adapter.in.web.exception;

import com.ttalkak.user.user.domain.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class CustomControllerAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("error", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(CustomValidationException e) {
        log.error("error message: {}", e.getMessage());
        // ! 핸들링 가능한 에러인 경우 200 상태코드로 응답
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.fail(e.getMessage(), HttpStatus.BAD_REQUEST.value(), e.getErrors()));
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ApiResponse<Void>> handleBadRequestException(Exception e) {
        log.error("error", e);
        // ! 핸들링 가능한 에러인 경우 200 상태코드로 응답
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.fail(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }
}
