package com.ttalkak.compute.compute.application.port.`in`

import com.ttalkak.compute.compute.domain.RunningStatus

data class DeploymentCommand(
    val deploymentId: Long,
    val status: RunningStatus,
    val useMemory: Int,
    val useCPU: Double,
    val runningTime: Int,
    val diskRead: Double,
    val diskWrite: Double,
)
