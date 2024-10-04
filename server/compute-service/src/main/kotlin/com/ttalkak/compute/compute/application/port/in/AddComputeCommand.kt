package com.ttalkak.compute.compute.application.port.`in`

import com.ttalkak.compute.compute.domain.DockerContainer

data class AddComputeCommand (
    val id: Long,
    val isDatabase: Boolean,
    val useMemory: Double,
    val useCPU: Double,
    val container: Any
)
