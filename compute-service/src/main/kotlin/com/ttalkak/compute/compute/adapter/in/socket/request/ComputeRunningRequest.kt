package com.ttalkak.compute.compute.adapter.`in`.socket.request

import com.ttalkak.compute.compute.domain.ComputerType
import com.ttalkak.compute.compute.domain.RunningStatus
import com.ttalkak.compute.compute.domain.ServiceType


data class ComputeRunningRequest(
    val status: RunningStatus,
    val serviceType: ServiceType,
    val port: Int,
    val message: String = ""
)
