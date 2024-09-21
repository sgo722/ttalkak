package com.ttalkak.compute.compute.application.port.out

import com.ttalkak.compute.compute.domain.RunningStatus

interface SaveDeploymentStatusPort {
    fun saveDeploymentStatus(
        userId: Long,
        deploymentId: Long,
        status: RunningStatus,
        useMemory: Int,
        useCPU: Double,
        runningTime: Int,
        diskRead: Double,
        diskWrite: Double,
    )
}