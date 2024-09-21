package com.ttalkak.compute.compute.adapter.out.cache.entity

import com.ttalkak.compute.compute.domain.ComputerType

data class ComputeUserCache (
    val userId: Long,
    val computeType: ComputerType,
    val usedCompute: Int,
    val usedMemory: Int,
    val usedCPU: Double
)