package com.ttalkak.compute.compute.application.port.`in`

import com.ttalkak.compute.compute.domain.ComputeRunning

interface LoadRunningUseCase {
    fun loadRunning(deploymentId: Long): ComputeRunning
}