package com.ttalkak.compute.compute.adapter.out.cache.repository

import jakarta.annotation.Resource
import org.springframework.data.redis.core.ListOperations
import org.springframework.stereotype.Repository

@Repository
class ComputePortCacheRepository {
    @Resource(name = "redisTemplate")
    private lateinit var listOperations: ListOperations<String, String>

    companion object {
        const val COMPUTE_PORT_CACHE_KEY = "computePortCache"
    }

    fun save(userId: Long, port: Int) {
        listOperations.leftPush(key(userId), port.toString())
    }

    fun save(userId: Long, ports: List<Int>) {
        if (ports.isEmpty()) {
            return
        }
        listOperations.leftPushAll(key(userId), ports.map { it.toString() })
    }

    fun delete(userId: Long, port: Int) {
        listOperations.remove(key(userId), 1, port.toString())
    }

    fun delete(userId: Long) {
        listOperations.operations.delete(key(userId))
    }

    fun findAll(userId: Long): List<Int> {
        return listOperations.range(key(userId), 0, -1)?.map { it.toInt() } ?: emptyList()
    }

    private fun key(userId: Long): String = "$COMPUTE_PORT_CACHE_KEY:$userId"
}