package com.ttalkak.compute.compute.domain

data class UserCreateEvent(
    val userId: Long,
    val username: String,
    val email: String,
    val address: String?
)
