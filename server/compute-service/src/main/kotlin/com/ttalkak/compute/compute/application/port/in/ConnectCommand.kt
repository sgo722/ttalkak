package com.ttalkak.compute.compute.application.port.`in`

import com.ttalkak.compute.compute.domain.ComputerType

data class ConnectCommand(
    val userId: Long,
    val computeType: ComputerType,
    val usedCompute: Int,
    val usedMemory: Double,
    val usedCPU: Double,
)
