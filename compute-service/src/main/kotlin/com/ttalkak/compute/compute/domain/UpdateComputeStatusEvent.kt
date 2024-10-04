package com.ttalkak.compute.compute.domain

data class UpdateComputeStatusEvent(
    val deploymentId: Long,
    val serviceType: ServiceType,
    val command: RunningCommand
)
