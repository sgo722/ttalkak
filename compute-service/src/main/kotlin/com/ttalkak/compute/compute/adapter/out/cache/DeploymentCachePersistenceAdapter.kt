package com.ttalkak.compute.compute.adapter.out.cache

import com.ttalkak.compute.common.PersistenceAdapter
import com.ttalkak.compute.compute.adapter.out.cache.entity.DeploymentStatusCache
import com.ttalkak.compute.compute.adapter.out.cache.repository.DeploymentCacheRepository
import com.ttalkak.compute.compute.application.port.out.SaveDeploymentStatusPort
import com.ttalkak.compute.compute.domain.RunningStatus

@PersistenceAdapter
class DeploymentCachePersistenceAdapter(
    private val deploymentCacheRepository: DeploymentCacheRepository
): SaveDeploymentStatusPort {
    override fun saveDeploymentStatus(
        userId: Long,
        deploymentId: Long,
        status: RunningStatus,
        useMemory: Int,
        useCPU: Double,
        runningTime: Int,
        diskRead: Double,
        diskWrite: Double
    ) {
        val deployment = DeploymentStatusCache(
            status = status,
            useMemory = useMemory,
            useCPU = useCPU,
            runningTime = runningTime,
            diskRead = diskRead,
            diskWrite = diskWrite

        )
        deploymentCacheRepository.save(deploymentId, deployment)
    }
}