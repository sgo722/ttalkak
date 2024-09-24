package com.ttalkak.compute.compute.adapter.out.cache.repository

import jakarta.annotation.Resource
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Repository

@Repository
class ConnectCacheRepository {
    @Resource(name = "redisTemplate")
    private lateinit var hashOperations: HashOperations<String, String, String>

    companion object {
        const val CONNECT_CACHE_KEY = "computeConnectCache"
    }

    fun save(userId: Long, sessionId: String) {
        hashOperations.put(CONNECT_CACHE_KEY, userId.toString(), sessionId)
    }

    fun exists(userId: Long): Boolean {
        return hashOperations.hasKey(CONNECT_CACHE_KEY, userId.toString())
    }

    fun delete(sessionId: String) {
        hashOperations.entries(CONNECT_CACHE_KEY).filter { it.value == sessionId }.forEach {
            hashOperations.delete(CONNECT_CACHE_KEY, it.key)
        }
    }

    fun delete(userId: Long) {
        hashOperations.delete(CONNECT_CACHE_KEY, userId.toString())
    }
}