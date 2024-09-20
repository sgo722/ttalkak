package com.ttalkak.compute.compute.domain

data class ComputeUser(
    val userId: Long,
    val computeType: ComputerType,
    val remainCompute: Int,
    val remainMemory: Int,
    val remainCPU: Double
)
