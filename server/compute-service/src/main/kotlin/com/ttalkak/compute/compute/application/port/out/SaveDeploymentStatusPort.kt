package com.ttalkak.compute.compute.application.port.out

import com.ttalkak.compute.compute.domain.RunningStatus
import com.ttalkak.compute.compute.domain.ServiceType

interface SaveDeploymentStatusPort {
    fun saveDeploymentStatus(
        userId: Long,
        id: Long,
        serviceType: ServiceType,
        status: RunningStatus,
        useMemory: Int,
        useCPU: Double,
        runningTime: Int,
        diskRead: Double,
        diskWrite: Double,
    )
}