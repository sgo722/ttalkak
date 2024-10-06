package com.ttalkak.compute.compute.adapter.out.cache.repository

import com.ttalkak.compute.common.util.Json
import com.ttalkak.compute.compute.adapter.out.cache.entity.DeploymentStatusCache
import com.ttalkak.compute.compute.domain.ServiceType
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

    fun save(id: Long, serviceType: ServiceType, deploymentStatus: DeploymentStatusCache) {
        val value = Json.serialize(deploymentStatus)
        hashOperations.put(DEPLOYMENT_CACHE_KEY, key(id, serviceType), value)
    }

    fun delete(id: Long, serviceType: ServiceType) {
        hashOperations.delete(DEPLOYMENT_CACHE_KEY, key(id, serviceType))
    }

    fun deleteByUserId(userId: Long) {
        if (userId == 0L) return

        hashOperations.keys(DEPLOYMENT_CACHE_KEY).forEach {
            val value = hashOperations.get(DEPLOYMENT_CACHE_KEY, it).toString()
            val deploymentStatus = Json.deserialize(value, DeploymentStatusCache::class.java)
            log.info {
                "DeploymentStatus 삭제(userId: $userId): $it, $deploymentStatus"
            }
            if (deploymentStatus.userId == userId) {
                hashOperations.delete(DEPLOYMENT_CACHE_KEY, it)
            }
        }
    }

    private fun key(id: Long, serviceType: ServiceType) = "$id-$serviceType"
}