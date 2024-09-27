package com.ttalkak.compute.compute.application.service

import com.ttalkak.compute.common.UseCase
import com.ttalkak.compute.compute.application.port.`in`.ComputeUseCase
import com.ttalkak.compute.compute.application.port.`in`.ConnectUseCase
import com.ttalkak.compute.compute.application.port.`in`.DisconnectScheduleUseCase
import com.ttalkak.compute.compute.application.port.out.CreateConnectPort
import com.ttalkak.compute.compute.application.port.out.LoadConnectPort
import com.ttalkak.compute.compute.application.port.out.RemoveConnectPort
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled

@UseCase
class ConnectService (
    private val createConnectPort: CreateConnectPort,
    private val removeConnectPort: RemoveConnectPort,
    private val loadConnectPort: LoadConnectPort,
    private val computeUseCase: ComputeUseCase
): ConnectUseCase, DisconnectScheduleUseCase {
    private val log = KotlinLogging.logger {}

    override fun connect(userId: Long, sessionId: String) {
        createConnectPort.connect(userId, sessionId)
    }

    override fun disconnect(sessionId: String): Long {
        return removeConnectPort.disconnect(sessionId)
    }

    @Scheduled(fixedDelay = 1000 * 60)
    override fun disconnectSchedule() {
        loadConnectPort.loadConnectUser().forEach {
            log.info {
                "스케쥴러에 의해 연결이 해제되었습니다. userId: $it"
            }

            removeConnectPort.disconnect(it)
            computeUseCase.disconnect(it)
        }
    }
}