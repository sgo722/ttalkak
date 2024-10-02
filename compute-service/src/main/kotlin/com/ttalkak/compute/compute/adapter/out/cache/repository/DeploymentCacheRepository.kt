package com.ttalkak.compute.compute.adapter.out.cache.repository

import com.ttalkak.compute.common.util.Json
import com.ttalkak.compute.compute.adapter.out.cache.entity.DeploymentStatusCache
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.Resource
import org.springframework.data.redis.core.HashOperations
import org.springframework.stereotype.Repository

@Repository
class DeploymentCacheRepository {
    companion object {
        const val DEPLOYMENT_CACHE_KEY = "deploymentCache"
    }

    @Resource(name = "redisTemplate")
    private lateinit var hashOperations: HashOperations<String, String, String>

    private val log = KotlinLogging.logger { }

    fun save(deploymentId: Long, deploymentStatus: DeploymentStatusCache) {
        val value = Json.serialize(deploymentStatus)
        hashOperations.put(DEPLOYMENT_CACHE_KEY, deploymentId.toString(), value)
    }

    fun delete(deploymentId: Long) {
        hashOperations.delete(DEPLOYMENT_CACHE_KEY, deploymentId.toString())
    }

    fun deleteByUserId(userId: Long) {
        hashOperations.keys(DEPLOYMENT_CACHE_KEY).forEach {
            val value = hashOperations.get(DEPLOYMENT_CACHE_KEY, it).toString()
            val deploymentStatus = Json.deserialize(value, DeploymentStatusCache::class.java)
            log.info {
                "RunningCache 삭제: $it, $deploymentStatus"
            }
            if (deploymentStatus.userId == userId) {
                hashOperations.delete(DEPLOYMENT_CACHE_KEY, it)
            }
        }
    }
}