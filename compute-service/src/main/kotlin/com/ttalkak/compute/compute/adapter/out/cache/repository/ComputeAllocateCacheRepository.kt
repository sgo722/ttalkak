package com.ttalkak.compute.compute.adapter.out.cache.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ttalkak.compute.common.util.Json
import com.ttalkak.compute.compute.adapter.out.cache.entity.ComputeAllocateCache
import com.ttalkak.compute.compute.domain.DockerContainer
import com.ttalkak.compute.compute.domain.DockerDatabaseContainer
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.Resource
import org.springframework.data.redis.core.ValueOperations
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.concurrent.TimeUnit

@Repository
class ComputeAllocateCacheRepository {
    @Resource(name = "redisTemplate")
    private lateinit var zSetOperations: ZSetOperations<String, String>

    private val log = KotlinLogging.logger {}

    companion object {
        const val COMPUTE_ALLOCATE_CACHE_KEY = "computeAllocateCache"
    }

    fun add(cache: ComputeAllocateCache, priority: Double) {
        zSetOperations.add(COMPUTE_ALLOCATE_CACHE_KEY, Json.serialize(cache), priority)
    }

    fun poll(): Optional<ComputeAllocateCache> {
        val cache = zSetOperations.range(COMPUTE_ALLOCATE_CACHE_KEY, 0, 0)?.firstOrNull()
        return cache?.let {
            zSetOperations.remove(COMPUTE_ALLOCATE_CACHE_KEY, it)

            return it.toContainer()
        } ?: Optional.empty()
    }

    fun peek(): Optional<ComputeAllocateCache> {
        val cache = zSetOperations.range(COMPUTE_ALLOCATE_CACHE_KEY, 0, 0)?.firstOrNull()
        return cache?.let {
            return it.toContainer()
        } ?: Optional.empty()
    }

    fun size(): Long {
        return zSetOperations.size(COMPUTE_ALLOCATE_CACHE_KEY) ?: 0
    }

    fun findDeploymentIds(): List<Long> {
        return zSetOperations.range(COMPUTE_ALLOCATE_CACHE_KEY, 0, -1)?.map {
            Json.deserialize(it, ComputeAllocateCache::class.java)
        }?.map { it.id } ?: emptyList()
    }

    private fun String.toContainer(): Optional<ComputeAllocateCache> {
        val container = Json.deserialize(this, ComputeAllocateCache::class.java)

        log.debug {
            "인스턴스 정보: ${container.instance}"
        }

        val instance: Any = when(container.isDatabase) {
            true -> Json.deserialize(container.instance.toString(), DockerDatabaseContainer::class.java)
            false -> Json.deserialize(container.instance.toString(), DockerContainer::class.java)
        }

        return Optional.of(container.copy(instance = instance))
    }
}