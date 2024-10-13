package com.ttalkak.compute.compute.application.port.out

import com.ttalkak.compute.compute.adapter.out.cache.entity.ComputeAllocateCache
import com.ttalkak.compute.compute.domain.ServiceType

interface SaveInstancePort {
    fun saveInstance(serviceId: Long, serviceType: ServiceType, cache: ComputeAllocateCache)

    fun deleteInstance(serviceId: Long, serviceType: ServiceType)
}