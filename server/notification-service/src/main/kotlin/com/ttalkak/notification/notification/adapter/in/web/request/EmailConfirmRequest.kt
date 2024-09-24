package com.ttalkak.notification.notification.adapter.`in`.web.request

data class EmailConfirmRequest(
    val userId: Long,
    val email: String,
    val code: String
)
