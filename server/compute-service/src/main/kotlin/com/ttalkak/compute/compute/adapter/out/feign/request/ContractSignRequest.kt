package com.ttalkak.compute.compute.adapter.out.feign.request

import com.ttalkak.compute.compute.domain.ServiceType

data class ContractSignRequest(
    val serviceId: Long,
    val serviceType: ServiceType,
    val senderId: Long,
    val recipientId: Long,
    val address: String
)
