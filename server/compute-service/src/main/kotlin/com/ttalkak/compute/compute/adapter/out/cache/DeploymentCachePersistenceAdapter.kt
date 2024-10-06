package com.ttalkak.compute.compute.adapter.out.cache

import com.ttalkak.compute.common.PersistenceAdapter
import com.ttalkak.compute.compute.adapter.out.cache.entity.DeploymentStatusCache
import com.ttalkak.compute.compute.adapter.out.cache.repository.DeploymentCacheRepository
import com.ttalkak.compute.compute.application.port.out.RemoveDeploymentStatusPort
import com.ttalkak.compute.compute.application.port.out.SaveDeploymentStatusPort
import com.ttalkak.compute.compute.domain.RunningStatus
import com.ttalkak.compute.compute.domain.ServiceType

@PersistenceAdapter
class DeploymentCachePersistenceAdapter(
    private val deploymentCacheRepository: DeploymentCacheRepository
): SaveDeploymentStatusPort, RemoveDeploymentStatusPort {
    override fun saveDeploymentStatus(
        userId: Long,
        id: Long,
        serviceType: ServiceType,
        status: RunningStatus,
        useMemory: Int,
        useCPU: Double,
        runningTime: Int,
        diskRead: Double,
        diskWrite: Double
    ) {
        val deployment = DeploymentStatusCache(
            status = status,
            userId = userId,
            useMemory = useMemory,
            useCPU = useCPU,
            runningTime = runningTime,
            diskRead = diskRead,
            diskWrite = diskWrite
        )
        deploymentCacheRepository.save(id, serviceType, deployment)
    }

    override fun removeDeploymentStatus(id: Long, serviceType: ServiceType) {
        deploymentCacheRepository.delete(id, serviceType)
    }

    override fun removeDeploymentStatusByUserId(userId: Long) {
        deploymentCacheRepository.deleteByUserId(userId)
    }
}