package com.ttalkak.compute.compute.application.port.out

import com.ttalkak.compute.compute.domain.DockerContainer

interface CreateAllocatePort {
    fun append(
        id: Long,
        isDatabase: Boolean,
        useMemory: Double,
        useCPU: Double,
        instance: Any
    )

    fun appendPriority(
        id: Long,
        rebuild: Boolean,
        isDatabase: Boolean,
        useMemory: Double,
        useCPU: Double,
        instance: Any
    )
}