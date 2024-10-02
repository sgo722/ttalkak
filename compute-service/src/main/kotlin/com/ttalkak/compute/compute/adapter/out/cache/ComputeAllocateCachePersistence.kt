package com.ttalkak.compute.compute.adapter.out.cache

import com.ttalkak.compute.common.PersistenceAdapter
import com.ttalkak.compute.compute.adapter.out.cache.entity.ComputeAllocateCache
import com.ttalkak.compute.compute.adapter.out.cache.repository.ComputeAllocateCacheRepository
import com.ttalkak.compute.compute.application.port.out.CreateAllocatePort
import com.ttalkak.compute.compute.application.port.out.LoadAllocatePort
import com.ttalkak.compute.compute.domain.DockerContainer
import java.util.*

@PersistenceAdapter
class ComputeAllocateCachePersistence (
    private val computeAllocateCacheRepository: ComputeAllocateCacheRepository
): CreateAllocatePort, LoadAllocatePort {
    companion object {
        const val PRIORITY_WEIGHT = 1_000_000_000 // 11.57 days
    }

    override fun append(
        deploymentId: Long,
        count: Int,
        useMemory: Double,
        useCPU: Double,
        instances: List<DockerContainer>
    ) {
        val priority = System.currentTimeMillis().toDouble()
        val compute = ComputeAllocateCache(
            deploymentId = deploymentId,
            rebuild = false,
            count = count,
            useMemory = useMemory,
            useCPU = useCPU,
            instances = instances
        )
        computeAllocateCacheRepository.add(compute, priority)
    }

    override fun appendPriority(
        deploymentId: Long,
        rebuild: Boolean,
        count: Int,
        useMemory: Double,
        useCPU: Double,
        instances: List<DockerContainer>
    ) {
        val priority = System.currentTimeMillis().toDouble() - PRIORITY_WEIGHT
        val compute = ComputeAllocateCache(
            deploymentId = deploymentId,
            rebuild = rebuild,
            count = count,
            useMemory = useMemory,
            useCPU = useCPU,
            instances = instances
        )
        computeAllocateCacheRepository.add(compute, priority)
    }

    override fun findFirst(): ComputeAllocateCache {
        return computeAllocateCacheRepository.peek().orElseThrow {
            NoSuchElementException("할당 대기중인 컴퓨터가 없습니다.")
        }
    }

    override fun pop(): Optional<ComputeAllocateCache> {
        return computeAllocateCacheRepository.poll()
    }

    override fun size(): Long {
        return computeAllocateCacheRepository.size()
    }

    override fun findDeploymentIds(): List<Long> {
        return computeAllocateCacheRepository.findDeploymentIds()
    }
}