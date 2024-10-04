package com.ttalkak.compute.compute.adapter.out.feign.request

import com.ttalkak.compute.compute.domain.RunningStatus
import com.ttalkak.compute.compute.domain.ServiceType

data class DeploymentUpdateStatusRequest(
    val id: Long,
    val serviceType: ServiceType,
    val status: RunningStatus,
    val message: String,
)
