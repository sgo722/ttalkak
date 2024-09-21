package com.ttalkak.compute.compute.adapter.out.cache.entity

import com.ttalkak.compute.compute.domain.RunningStatus

data class DeploymentStatusCache (
    val status: RunningStatus,
    val useMemory: Int,
    val useCPU: Double,
    val runningTime: Int,
    val diskRead: Double,
    val diskWrite: Double,
)
