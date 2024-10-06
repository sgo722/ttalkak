package com.ttalkak.compute.compute.application.port.`in`

import com.ttalkak.compute.compute.domain.RunningStatus
import com.ttalkak.compute.compute.domain.ServiceType

data class DeploymentCommand(
    val id: Long,
    val serviceType: ServiceType,
    val status: RunningStatus,
    val useMemory: Int,
    val useCPU: Double,
    val runningTime: Int,
    val diskRead: Double,
    val diskWrite: Double,
)
