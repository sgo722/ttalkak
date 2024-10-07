package com.ttalkak.compute.compute.adapter.`in`.socket

import com.ttalkak.compute.common.SocketAdapter
import com.ttalkak.compute.compute.adapter.`in`.socket.request.ComputeConnectRequest
import com.ttalkak.compute.compute.adapter.`in`.socket.request.ComputeRunningRequest
import com.ttalkak.compute.compute.adapter.`in`.socket.request.ComputeStatusRequest
import com.ttalkak.compute.compute.application.port.`in`.*
import com.ttalkak.compute.compute.domain.RunningStatus
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller

@SocketAdapter
@Controller
class ComputeSocketController(
    private val computeUseCase: ComputeUseCase,
    private val computeStatusUseCase: ComputeStatusUseCase,
    private val upsertRunningUseCase: UpsertRunningUseCase,
    private val contractSignUseCase: ContractSignUseCase,
) {
    private val log = KotlinLogging.logger {  }
    
    @MessageMapping("/compute/connect")
    fun compute(@Payload request: ComputeConnectRequest) {
        log.info { 
            "컴퓨터 연결 요청 (/compute/connect): $request"
        }

        val command = ConnectCommand(
            userId = request.userId,
            computeType = request.computerType,
            usedCompute = request.usedCompute,
            usedMemory = (request.usedMemory / 1_000_000_000.0),
            usedCPU = request.usedCPU,
            ports = request.ports
        )

        computeUseCase.connect(command)
    }

    /**
     * Compute 상태를 확인하는 ping 요청을 처리
     *
     * @param request Compute 상태 요청
     */
    @MessageMapping("/compute/ping")
    fun ping(@Payload request: ComputeStatusRequest) {
        log.info {
            "컴퓨터 실시간 데이터 요청 (/compute/ping): $request"
        }
        
        val command = StatusUpdateCommand(
            userId = request.userId,
            computeType = request.computerType,
            usedCompute = request.usedCompute,
            usedMemory = (request.usedMemory / 1_000_000_000.0),
            usedCPU = request.usedCPU
        )

        val deploymentCommands = request.deployments.map {
            DeploymentCommand(
                id = it.id,
                serviceType = it.serviceType,
                status = it.status,
                useMemory = it.useMemory,
                useCPU = it.useCPU,
                runningTime = it.runningTime,
                diskRead = it.diskRead,
                diskWrite = it.diskWrite,
            )
        }

        computeStatusUseCase.update(command, deploymentCommands)
    }

    /**
     * 신규 인스턴스 생성 요청에 대한 응답을 처리
     *
     * @param request Compute 상태 요청
     */
    @MessageMapping("/compute/{id}/status")
    fun status(
        @DestinationVariable id: Long,
        @Header("X-USER-ID") userId: Long,
        @Payload request: ComputeRunningRequest
    ) {
        log.info {
            "컴퓨터 생성 응답 요청 (/compute/$id/status): $request"
        }

        val command = RunningCommand(
            id = id,
            serviceType = request.serviceType,
            port = request.port,
            status = request.status,
            message = request.message
        )

        upsertRunningUseCase.upsertRunning(userId, command)

        if (request.status == RunningStatus.RUNNING) {
            contractSignUseCase.sign(
                serviceId = id,
                serviceType = request.serviceType,
                senderId = request.senderId,
                recipientId = userId
            )
        }
    }
}