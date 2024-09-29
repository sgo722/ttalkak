package com.ttalkak.compute.compute.adapter.out.cache.repository

import com.ttalkak.compute.compute.adapter.out.cache.entity.ComputeAllocateCache
import jakarta.annotation.Resource
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
class ComputeAllocateCacheRepository {
    @Resource(name = "redisTemplate")
    private lateinit var zSetOperations: ZSetOperations<String, ComputeAllocateCache>

    companion object {
        const val COMPUTE_ALLOCATE_CACHE_KEY = "computeAllocateCache"
    }

    fun add(cache: ComputeAllocateCache, priority: Double) {
        zSetOperations.add(COMPUTE_ALLOCATE_CACHE_KEY, cache, priority)
    }

    fun poll(): Optional<ComputeAllocateCache> {
        val cache = zSetOperations.range(COMPUTE_ALLOCATE_CACHE_KEY, 0, 0)?.firstOrNull()
        return cache?.let {
            zSetOperations.remove(COMPUTE_ALLOCATE_CACHE_KEY, it)

            return Optional.of(it)
        } ?: Optional.empty()
    }

    fun size(): Long {
        return zSetOperations.size(COMPUTE_ALLOCATE_CACHE_KEY) ?: 0
    }
}