package com.ttalkak.compute.compute.adapter.out.feign.request

import com.ttalkak.compute.compute.domain.RunningStatus

data class DeploymentUpdateStatusRequest(
    val deploymentId: Long,
    val status: RunningStatus,
    val message : String?
)
