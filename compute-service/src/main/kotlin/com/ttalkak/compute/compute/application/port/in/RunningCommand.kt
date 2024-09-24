package com.ttalkak.compute.compute.application.port.`in`

import com.ttalkak.compute.compute.domain.RunningStatus

data class RunningCommand (
    val deploymentId: Long,
    val port: Int,
    val status: RunningStatus,
    val message: String?
)