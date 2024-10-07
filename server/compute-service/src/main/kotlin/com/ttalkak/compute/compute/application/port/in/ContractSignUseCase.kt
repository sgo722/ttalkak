package com.ttalkak.compute.compute.application.port.`in`

import com.ttalkak.compute.compute.domain.ServiceType

interface ContractSignUseCase {
    fun sign(
        serviceId: Long,
        serviceType: ServiceType,
        senderId: Long,
        recipientId: Long,
    )
}