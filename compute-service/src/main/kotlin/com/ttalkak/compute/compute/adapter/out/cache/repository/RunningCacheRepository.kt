package com.ttalkak.compute.compute.adapter.out.cache.repository

import com.ttalkak.compute.common.util.Json
import com.ttalkak.compute.compute.adapter.out.cache.entity.DeploymentStatusCache
import com.ttalkak.compute.compute.adapter.out.cache.entity.RunningCache
import com.ttalkak.compute.compute.domain.ComputeInstance
import com.ttalkak.compute.compute.domain.ServiceType
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.Resource
import org.springframework.data.redis.core.HashOperations
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
class RunningCacheRepository {
    companion object {
        const val RUNNING_CACHE_KEY = "runningCache"
    }

    private val log = KotlinLogging.logger { }

    @Resource(name = "redisTemplate")
    private lateinit var hashOperations: HashOperations<String, String, String>

    fun save(id: Long, serviceType: ServiceType, runningCache: RunningCache) {
        val value = Json.serialize(runningCache)
        hashOperations.put(RUNNING_CACHE_KEY, key(id, serviceType), value)
    }

    fun delete(id: Long, serviceType: ServiceType) {
        hashOperations.delete(RUNNING_CACHE_KEY, key(id, serviceType))
    }

    fun findByUserId(userId: Long): List<ComputeInstance> {
        return hashOperations.keys(RUNNING_CACHE_KEY).filter {
            val value = hashOperations.get(RUNNING_CACHE_KEY, it).toString()
            val runningCache = Json.deserialize(value, RunningCache::class.java)
            runningCache.userId == userId
        }.map {
            val key = it.split("-")
            ComputeInstance(
                id = key[0].toLong(),
                serviceType = ServiceType.valueOf(key[1])
            )
        }
    }

    fun deleteByUserId(userId: Long) {
        if (userId == 0L) return

        hashOperations.keys(RUNNING_CACHE_KEY).forEach {
            val value = hashOperations.get(RUNNING_CACHE_KEY, it).toString()
            val runningCache = Json.deserialize(value, RunningCache::class.java)
            log.info {
                "RunningCache 삭제(userId: $userId): $it, $runningCache"
            }
            if (runningCache.userId == userId) {
                hashOperations.delete(RUNNING_CACHE_KEY, it)
            }
        }
    }

    fun findById(id: Long, serviceType: ServiceType): Optional<RunningCache> {
        val value = hashOperations.get(RUNNING_CACHE_KEY, key(id, serviceType)).toString()
        return Optional.ofNullable(value).map {
            Json.deserialize(it, RunningCache::class.java)
        }
    }

    private fun key(id: Long, serviceType: ServiceType) = "$id-$serviceType"
}