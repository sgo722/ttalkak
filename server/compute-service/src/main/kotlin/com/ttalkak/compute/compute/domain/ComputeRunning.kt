package com.ttalkak.compute.compute.domain

data class ComputeRunning(
    val userId: Long,
    val status: RunningStatus,
    val message: String?,
)
