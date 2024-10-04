package com.ttalkak.compute.compute.adapter.out.cache.entity

import com.ttalkak.compute.compute.domain.DockerContainer

data class ComputeAllocateCache (
    val deploymentId: Long,
    val count: Int,
    val rebuild: Boolean,
    val useMemory: Double,
    val useCPU: Double,
    val instance: DockerContainer
)
