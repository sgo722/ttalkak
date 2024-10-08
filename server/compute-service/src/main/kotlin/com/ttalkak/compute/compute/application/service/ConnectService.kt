package com.ttalkak.compute.compute.application.service

import com.ttalkak.compute.common.UseCase
import com.ttalkak.compute.compute.adapter.out.feign.DeploymentFeignClient
import com.ttalkak.compute.compute.adapter.out.feign.request.DeploymentUpdateStatusRequest
import com.ttalkak.compute.compute.application.port.`in`.ComputeUseCase
import com.ttalkak.compute.compute.application.port.`in`.ConnectUseCase
import com.ttalkak.compute.compute.application.port.`in`.DisconnectScheduleUseCase
import com.ttalkak.compute.compute.application.port.out.*
import com.ttalkak.compute.compute.domain.RunningStatus
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled

@UseCase
class ConnectService (
    private val createConnectPort: CreateConnectPort,
    private val removeConnectPort: RemoveConnectPort,
    private val saveComputePort: SaveComputePort,
    private val checkConnectPort: CheckConnectPort,
    private val removePortPort: RemovePortPort,
    private val removeRunningPort: RemoveRunningPort,
    private val loadRunningPort: LoadRunningPort,
    private val deploymentFeignClient: DeploymentFeignClient,
    private val removeDeploymentStatusPort: RemoveDeploymentStatusPort,
    private val loadComputePort: LoadComputePort
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
        loadComputePort.loadAllCompute().filter {
            !checkConnectPort.isConnected(it.userId)
        }.forEach {
            log.info {
                "스케쥴러에 의해 연결이 끊어진 사용자: ${it.userId}"
            }

            saveComputePort.deleteCompute(it.userId)
            removePortPort.removePort(it.userId)
            removeRunningPort.removeRunningByUserId(it.userId)
            removeDeploymentStatusPort.removeDeploymentStatusByUserId(it.userId)

            loadRunningPort.loadRunningByUserId(it.userId).forEach { compute ->
                val status = DeploymentUpdateStatusRequest(
                    id = compute.id,
                    serviceType = compute.serviceType,
                    status = RunningStatus.ERROR,
                    message = "노드 서버 연결이 끊어짐"
                )

                try {
                    deploymentFeignClient.updateStatus(status)
                } catch (e: Exception) {
                    log.error(e) { "간접 연결: 디플로이먼트 상태 업데이트 실패" }
                }
            }
        }
    }
}