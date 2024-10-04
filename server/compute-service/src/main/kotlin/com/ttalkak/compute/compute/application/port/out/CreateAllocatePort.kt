package com.ttalkak.compute.compute.application.port.out

import com.ttalkak.compute.compute.domain.DockerContainer

interface CreateAllocatePort {
    fun append(
        deploymentId: Long,
        count: Int,
        useMemory: Double,
        useCPU: Double,
        instance: DockerContainer
    )

    fun appendPriority(
        deploymentId: Long,
        rebuild: Boolean,
        count: Int,
        useMemory: Double,
        useCPU: Double,
        instance: DockerContainer
    )
}