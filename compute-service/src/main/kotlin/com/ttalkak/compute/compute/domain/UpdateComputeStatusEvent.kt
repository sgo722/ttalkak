package com.ttalkak.compute.compute.domain

data class UpdateComputeStatusEvent(
    val id: Long,
    val serviceType: ServiceType,
    val command: RunningCommand
)
