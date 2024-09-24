package com.ttalkak.compute.compute.application.port.out

import com.ttalkak.compute.compute.domain.ComputeRunning

interface LoadRunningPort {
    fun loadRunning(deploymentId: Long): ComputeRunning
}