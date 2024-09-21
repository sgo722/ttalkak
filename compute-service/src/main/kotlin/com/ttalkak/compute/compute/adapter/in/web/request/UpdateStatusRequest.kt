package com.ttalkak.compute.compute.adapter.`in`.web.request

data class UpdateStatusRequest(
    val maxCompute: Int,
    val availablePortStart: Int,
    val availablePortEnd: Int,
    val maxMemory: Int = 0,
    val maxCPU: Double = 0.0,
)
