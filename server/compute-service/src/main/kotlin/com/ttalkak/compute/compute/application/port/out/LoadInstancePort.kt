package com.ttalkak.compute.compute.application.port.out

import com.ttalkak.compute.compute.adapter.out.cache.entity.ComputeAllocateCache
import com.ttalkak.compute.compute.domain.ServiceType
import java.util.Optional

interface LoadInstancePort {
    fun loadInstance(serviceId: Long, serviceType: ServiceType): Optional<ComputeAllocateCache>
}