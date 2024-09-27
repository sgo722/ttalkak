package com.ttalkak.compute.compute.adapter.out.cache.repository

import com.ttalkak.compute.common.util.Json
import com.ttalkak.compute.compute.adapter.out.cache.entity.DeploymentStatusCache
import com.ttalkak.compute.compute.adapter.out.cache.entity.RunningCache
import jakarta.annotation.Resource
import org.springframework.data.redis.core.HashOperations
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
class RunningCacheRepository {
    companion object {
        const val RUNNING_CACHE_KEY = "runningCache"
    }

    @Resource(name = "redisTemplate")
    private lateinit var hashOperations: HashOperations<String, String, String>

    fun save(deploymentId: Long, runningCache: RunningCache) {
        val value = Json.serialize(runningCache)
        hashOperations.put(RUNNING_CACHE_KEY, deploymentId.toString(), value)
    }

    fun delete(deploymentId: Long) {
        hashOperations.delete(RUNNING_CACHE_KEY, deploymentId.toString())
    }

    fun deleteByUserId(userId: Long) {
        val keys = hashOperations.keys(RUNNING_CACHE_KEY)
        keys.forEach {
            val value = hashOperations.get(RUNNING_CACHE_KEY, it).toString()
            val runningCache = Json.deserialize(value, RunningCache::class.java)
            if (runningCache.userId == userId) {
                hashOperations.delete(RUNNING_CACHE_KEY, it)
            }
        }
    }

    fun findById(deploymentId: Long): Optional<RunningCache> {
        val value = hashOperations.get(RUNNING_CACHE_KEY, deploymentId.toString()).toString()
        return Optional.ofNullable(value).map {
            Json.deserialize(it, RunningCache::class.java)
        }
    }
}