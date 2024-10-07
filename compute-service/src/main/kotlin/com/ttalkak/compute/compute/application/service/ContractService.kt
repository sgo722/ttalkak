package com.ttalkak.compute.compute.application.service

import com.ttalkak.compute.common.UseCase
import com.ttalkak.compute.compute.adapter.out.feign.ContractFeignClient
import com.ttalkak.compute.compute.adapter.out.feign.request.ContractSignRequest
import com.ttalkak.compute.compute.application.port.`in`.ContractSignUseCase
import com.ttalkak.compute.compute.application.port.out.LoadStatusPort
import com.ttalkak.compute.compute.domain.ServiceType

@UseCase
class ContractService(
    private val contractFeignClient: ContractFeignClient,
    private val loadStatusPort: LoadStatusPort,
): ContractSignUseCase {
    override fun sign(serviceId: Long, serviceType: ServiceType, senderId: Long, recipientId: Long) {
        val userStatus = loadStatusPort.loadStatus(recipientId).orElseThrow {
            IllegalArgumentException("Recipient not found")
        }

        if (userStatus.address == null) {
            throw IllegalArgumentException("Recipient address not found")
        }

        val contractSignRequest = ContractSignRequest(
            serviceId = serviceId,
            serviceType = serviceType,
            senderId = senderId,
            recipientId = recipientId,
            address = userStatus.address,
        )
        try {
            contractFeignClient.sign(contractSignRequest)
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to sign contract")
        }
    }
}