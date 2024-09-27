package com.ttalkak.compute.compute.domain

data class UserStatus(
    val userId: Long,
    val maxCompute: Int,
    val maxMemory: Int,
    val maxCPU: Double,
    val availablePortStart: Int,
    val availablePortEnd: Int
)
