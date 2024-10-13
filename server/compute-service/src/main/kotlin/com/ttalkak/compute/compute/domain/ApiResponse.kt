package com.ttalkak.compute.compute.domain

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val status: Int,
    val data: T?
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(true, "OK", 200, data)
        }

        fun success(): ApiResponse<Any> {
            return ApiResponse(true, "OK", 200, null)
        }

        fun fail(message: String, status: Int, errors: Any): ApiResponse<Any> {
            return ApiResponse(false, message, status, errors)
        }
    }
}
