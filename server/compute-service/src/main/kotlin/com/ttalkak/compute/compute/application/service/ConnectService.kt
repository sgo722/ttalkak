package com.ttalkak.compute.compute.application.service

import com.ttalkak.compute.common.UseCase
import com.ttalkak.compute.compute.application.port.`in`.ConnectUseCase
import com.ttalkak.compute.compute.application.port.out.CreateConnectPort
import com.ttalkak.compute.compute.application.port.out.RemoveConnectPort

@UseCase
class ConnectService(
    private val createConnectPort: CreateConnectPort,
    private val removeConnectPort: RemoveConnectPort
): ConnectUseCase {
    override fun connect(userId: Long, sessionId: String) {
        createConnectPort.connect(userId, sessionId)
    }

    override fun disconnect(sessionId: String) {
        removeConnectPort.disconnect(sessionId)
    }
}