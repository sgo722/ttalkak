package com.ttalkak.compute.compute.adapter.`in`.socket.request

import com.ttalkak.compute.compute.domain.RunningStatus

data class DeploymentStatusRequest(
    val deploymentId: Long,
    val status: RunningStatus,
    val useMemory: Int,
    val useCPU: Double,
    val runningTime: Int,
    val diskRead: Double,
    val diskWrite: Double,
)
