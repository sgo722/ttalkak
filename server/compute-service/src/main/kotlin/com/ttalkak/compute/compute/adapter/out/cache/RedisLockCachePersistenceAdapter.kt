package com.ttalkak.compute.compute.adapter.out.cache

import com.ttalkak.compute.common.PersistenceAdapter
import com.ttalkak.compute.compute.adapter.out.cache.repository.RedisLockCacheRepository
import com.ttalkak.compute.compute.application.port.out.RedisLockPort

@PersistenceAdapter
class RedisLockCachePersistenceAdapter (
    private val redisLockCacheRepository: RedisLockCacheRepository
): RedisLockPort {
    override fun lock(key: String, expire: Long): Boolean {
        return redisLockCacheRepository.acquireLock(key, expire)
    }

    override fun unlock(key: String): Boolean {
        if (!isLockHold(key)) return false
        redisLockCacheRepository.releaseLock(key)
        return true
    }

    override fun isLockHold(key: String): Boolean {
        return redisLockCacheRepository.isLockHold(key)
    }
}