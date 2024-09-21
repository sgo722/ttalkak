package com.ttalkak.compute.compute.domain

data class UserStatus(
    val userId: Long,
    val maxCompute: Int,
    val availablePortStart: Int,
    val availablePortEnd: Int
)
