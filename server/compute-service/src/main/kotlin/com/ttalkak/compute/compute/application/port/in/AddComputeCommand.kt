package com.ttalkak.compute.compute.application.port.`in`

import com.ttalkak.compute.compute.domain.DockerContainer

data class AddComputeCommand(
    val deploymentId: Long,
    val computeCount: Int,
    val useMemory: Double,
    val useCPU: Double,
    val container: DockerContainer
)
