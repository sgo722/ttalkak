package com.ttalkak.compute.compute.application.port.`in`

import com.ttalkak.compute.compute.domain.RunningStatus
import com.ttalkak.compute.compute.domain.ServiceType

data class RunningCommand (
    val id: Long,
    val serviceType: ServiceType,
    val port: Int,
    val status: RunningStatus,
    val message: String?
)