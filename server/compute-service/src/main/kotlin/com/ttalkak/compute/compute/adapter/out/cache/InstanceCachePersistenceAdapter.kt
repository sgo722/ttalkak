package com.ttalkak.compute.compute.adapter.out.cache

import com.ttalkak.compute.common.PersistenceAdapter
import com.ttalkak.compute.compute.adapter.out.cache.entity.ComputeAllocateCache
import com.ttalkak.compute.compute.adapter.out.cache.repository.InstanceCacheRepository
import com.ttalkak.compute.compute.application.port.out.LoadInstancePort
import com.ttalkak.compute.compute.application.port.out.SaveInstancePort
import com.ttalkak.compute.compute.domain.ServiceType
import java.util.*

@PersistenceAdapter
class InstanceCachePersistenceAdapter (
    private val instanceCacheRepository: InstanceCacheRepository
): LoadInstancePort, SaveInstancePort {
    override fun loadInstance(serviceId: Long, serviceType: ServiceType): Optional<ComputeAllocateCache> {
        return instanceCacheRepository.get(serviceId, serviceType)
    }

    override fun saveInstance(serviceId: Long, serviceType: ServiceType, cache: ComputeAllocateCache) {
        instanceCacheRepository.add(serviceId, serviceType, cache)
    }

    override fun deleteInstance(serviceId: Long, serviceType: ServiceType) {
        instanceCacheRepository.remove(serviceId, serviceType)
    }
}