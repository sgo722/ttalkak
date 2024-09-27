package com.ttalkak.compute.compute.adapter.out.cache.repository

import com.ttalkak.compute.common.util.Json
import com.ttalkak.compute.compute.adapter.out.cache.entity.DeploymentStatusCache
import jakarta.annotation.Resource
import org.springframework.data.redis.core.HashOperations
import org.springframework.stereotype.Repository

@Repository
class DeploymentCacheRepository {
    @Resource(name = "redisTemplate")
    private lateinit var hashOperations: HashOperations<String, String, String>

    companion object {
        const val DEPLOYMENT_CACHE_KEY = "deploymentCache"
    }

    fun save(deploymentId: Long, deploymentStatus: DeploymentStatusCache) {
        val value = Json.serialize(deploymentStatus)
        hashOperations.put(DEPLOYMENT_CACHE_KEY, deploymentId.toString(), value)
    }

    fun delete(deploymentId: Long) {
        hashOperations.delete(DEPLOYMENT_CACHE_KEY, deploymentId.toString())
    }

    fun deleteByUserId(userId: Long) {
        val keys = hashOperations.keys(DEPLOYMENT_CACHE_KEY)
        keys.forEach {
            val value = hashOperations.get(DEPLOYMENT_CACHE_KEY, it).toString()
            val deploymentStatus = Json.deserialize(value, DeploymentStatusCache::class.java)
            if (deploymentStatus.userId == userId) {
                hashOperations.delete(DEPLOYMENT_CACHE_KEY, it)
            }
        }
    }
}