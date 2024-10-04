package com.ttalkak.compute.compute.adapter.out.cache.entity

import com.ttalkak.compute.compute.domain.DockerContainer

data class ComputeAllocateCache (
    val id: Long,
    val isDatabase: Boolean,
    val rebuild: Boolean,
    val useMemory: Double,
    val useCPU: Double,
    var instance: Any
)
