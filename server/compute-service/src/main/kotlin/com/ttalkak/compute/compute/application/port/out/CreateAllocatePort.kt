package com.ttalkak.compute.compute.application.port.out

import com.ttalkak.compute.compute.domain.DockerContainer

interface CreateAllocatePort {
    fun append(
        id: Long,
        senderId: Long,
        isDatabase: Boolean,
        useMemory: Double,
        useCPU: Double,
        instance: Any
    )

    fun appendPriority(
        id: Long,
        senderId: Long,
        rebuild: Boolean,
        isDatabase: Boolean,
        useMemory: Double,
        useCPU: Double,
        instance: Any
    )
}