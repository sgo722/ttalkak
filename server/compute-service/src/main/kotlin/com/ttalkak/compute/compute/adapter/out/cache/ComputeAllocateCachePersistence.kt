package com.ttalkak.compute.compute.adapter.out.cache

import com.ttalkak.compute.common.PersistenceAdapter
import com.ttalkak.compute.compute.adapter.out.cache.entity.ComputeAllocateCache
import com.ttalkak.compute.compute.adapter.out.cache.repository.ComputeAllocateCacheRepository
import com.ttalkak.compute.compute.application.port.out.SaveAllocatePort
import com.ttalkak.compute.compute.application.port.out.LoadAllocatePort
import java.util.*

@PersistenceAdapter
class ComputeAllocateCachePersistence (
    private val computeAllocateCacheRepository: ComputeAllocateCacheRepository
): SaveAllocatePort, LoadAllocatePort {
    companion object {
        const val PRIORITY_WEIGHT = 1_000_000_000 // 11.57 days
    }

    override fun append(
        id: Long,
        senderId: Long,
        isDatabase: Boolean,
        useMemory: Double,
        useCPU: Double,
        instance: Any
    ) {
        val priority = System.currentTimeMillis().toDouble()
        val compute = ComputeAllocateCache(
            id = id,
            senderId = senderId,
            rebuild = false,
            isDatabase = isDatabase,
            useMemory = useMemory,
            useCPU = useCPU,
            instance = instance
        )
        computeAllocateCacheRepository.add(compute, priority)
    }

    override fun  appendPriority(
        id: Long,
        senderId: Long,
        rebuild: Boolean,
        isDatabase: Boolean,
        useMemory: Double,
        useCPU: Double,
        instance: Any
    ) {
        val priority = System.currentTimeMillis().toDouble() - PRIORITY_WEIGHT
        val compute = ComputeAllocateCache(
            id = id,
            senderId = senderId,
            rebuild = rebuild,
            isDatabase = isDatabase,
            useMemory = useMemory,
            useCPU = useCPU,
            instance = instance
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