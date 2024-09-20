package com.ttalkak.compute.compute.application.port.out

import com.ttalkak.compute.compute.domain.RunningStatus

interface SaveRunningPort {
    fun saveRunning(
        userId: Long,
        deploymentId: Long,
        status: RunningStatus,
        message: String?
    )
}