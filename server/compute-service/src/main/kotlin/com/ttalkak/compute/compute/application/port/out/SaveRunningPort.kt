package com.ttalkak.compute.compute.application.port.out

import com.ttalkak.compute.compute.domain.RunningStatus
import com.ttalkak.compute.compute.domain.ServiceType

interface SaveRunningPort {
    fun saveRunning(
        userId: Long,
        id: Long,
        serviceType: ServiceType,
        port: Int,
        status: RunningStatus,
        message: String?
    )
}