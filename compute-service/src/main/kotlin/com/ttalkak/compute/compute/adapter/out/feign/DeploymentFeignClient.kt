package com.ttalkak.compute.compute.adapter.out.feign

import com.ttalkak.compute.compute.adapter.out.feign.request.DeploymentUpdateStatusRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping


@FeignClient(name = "DEPLOYMENT-SERVICE")
interface DeploymentFeignClient {
    @PostMapping("/feign/deployment/status")
    fun updateStatus(request: DeploymentUpdateStatusRequest)
}