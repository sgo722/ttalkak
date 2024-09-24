package com.ttalkak.notification.notification.domain

data class EmailConfirmEvent (
    val userId: Long,
    val email: String,
)
