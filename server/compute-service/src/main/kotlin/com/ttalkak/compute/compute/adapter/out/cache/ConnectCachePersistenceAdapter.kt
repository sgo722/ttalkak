package com.ttalkak.compute.compute.adapter.out.cache

import com.ttalkak.compute.common.PersistenceAdapter
import com.ttalkak.compute.compute.adapter.out.cache.repository.ConnectCacheRepository
import com.ttalkak.compute.compute.application.port.out.CheckConnectPort
import com.ttalkak.compute.compute.application.port.out.CreateConnectPort
import com.ttalkak.compute.compute.application.port.out.LoadConnectPort
import com.ttalkak.compute.compute.application.port.out.RemoveConnectPort

@PersistenceAdapter
class ConnectCachePersistenceAdapter(
    private val connectCacheRepository: ConnectCacheRepository
): CreateConnectPort, CheckConnectPort, RemoveConnectPort, LoadConnectPort {
    override fun connect(userId: Long, sessionId: String) {
        connectCacheRepository.save(userId, sessionId)
    }

    override fun disconnect(sessionId: String): Long {
        val userId = connectCacheRepository.find(sessionId)
        connectCacheRepository.delete(sessionId)
        return userId ?: throw IllegalArgumentException("Not found userId")
    }

    override fun disconnect(userId: Long) {
        connectCacheRepository.delete(userId)
    }

    override fun isConnected(userId: Long): Boolean {
        return connectCacheRepository.exists(userId)
    }

    override fun loadConnectUser(): List<Long> {
        return connectCacheRepository.findKeys().map { it.toLong() }
    }
}