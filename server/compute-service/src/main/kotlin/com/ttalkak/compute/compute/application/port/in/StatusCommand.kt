package com.ttalkak.compute.compute.application.port.`in`

data class StatusCommand(
    val maxCompute: Int,
    val maxMemory: Int,
    val maxCPU: Double,
    val availablePortStart: Int,
    val availablePortEnd: Int
)
