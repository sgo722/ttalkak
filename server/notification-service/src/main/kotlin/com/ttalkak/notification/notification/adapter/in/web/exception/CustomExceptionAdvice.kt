package com.ttalkak.notification.notification.adapter.`in`.web.exception

import com.ttalkak.notification.notification.domain.ApiResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionAdvice {
    private val log = KotlinLogging.logger {  }

    @ResponseBody
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ApiResponse<Void> {
        log.error(e) { "에러 발생: ${e.message}" }
        return ApiResponse.fail(e.message ?: "알 수 없는 오류가 발생했습니다.", 500)
    }
}