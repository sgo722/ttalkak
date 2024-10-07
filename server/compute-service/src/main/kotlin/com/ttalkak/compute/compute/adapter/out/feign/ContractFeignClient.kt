package com.ttalkak.compute.compute.adapter.out.feign

import com.ttalkak.compute.compute.adapter.out.feign.request.ContractSignRequest
import com.ttalkak.compute.compute.adapter.out.feign.request.DeploymentUpdateStatusRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "CONTRACT-SERVICE")
interface ContractFeignClient {
    @PostMapping("/v1/contract/sign")
    fun sign(request: ContractSignRequest)
}