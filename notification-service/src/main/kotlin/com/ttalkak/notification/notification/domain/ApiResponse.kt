package com.ttalkak.notification.notification.domain

data class ApiResponse<T> (
    val success: Boolean,
    val message: String,
    val status: Int,
    val data: T?
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(true, "OK", 200, data)
        }

        fun empty(): ApiResponse<Void> {
            return ApiResponse(true, "OK", 200, null)
        }

        fun <T> fail(message: String, status: Int): ApiResponse<Nothing?> {
            return ApiResponse(false, message, status, null)
        }
    }
}
