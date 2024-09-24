package com.ttalkak.compute.compute.adapter.out.cache

import com.ttalkak.compute.common.PersistenceAdapter
import com.ttalkak.compute.compute.adapter.out.cache.repository.ConnectCacheRepository
import com.ttalkak.compute.compute.application.port.out.CheckConnectPort
import com.ttalkak.compute.compute.application.port.out.CreateConnectPort
import com.ttalkak.compute.compute.application.port.out.RemoveConnectPort

@PersistenceAdapter
class ConnectCachePersistenceAdapter(
    private val connectCacheRepository: ConnectCacheRepository
): CreateConnectPort, CheckConnectPort, RemoveConnectPort {
    override fun connect(userId: Long, sessionId: String) {
        connectCacheRepository.save(userId, sessionId)
    }

    override fun disconnect(sessionId: String) {
        connectCacheRepository.delete(sessionId)
    }

    override fun disconnect(userId: Long) {
        connectCacheRepository.delete(userId)
    }

    override fun isConnected(userId: Long): Boolean {
        return connectCacheRepository.exists(userId)
    }
}