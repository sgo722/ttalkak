package com.ttalkak.compute.compute.adapter.out.cache.repository

import jakarta.annotation.Resource
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class RedisLockCacheRepository {
    @Resource(name = "redisTemplate")
    private lateinit var valueOperations: ValueOperations<String, String>

    fun acquireLock(key: String, expire: Long): Boolean {
        return valueOperations.setIfAbsent(key, "LOCKED", expire, TimeUnit.MILLISECONDS) == true
    }

    fun releaseLock(key: String) {
        valueOperations.operations.delete(key)
    }

    fun isLockHold(key: String): Boolean {
        return valueOperations.get(key)?.let {
            return true
        } ?: false
    }
}