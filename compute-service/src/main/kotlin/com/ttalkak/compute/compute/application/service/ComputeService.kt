package com.ttalkak.compute.compute.application.service

import com.ttalkak.compute.common.UseCase
import com.ttalkak.compute.compute.adapter.out.feign.DeploymentFeignClient
import com.ttalkak.compute.compute.adapter.out.feign.request.DeploymentUpdateStatusRequest
import com.ttalkak.compute.compute.application.port.`in`.*
import com.ttalkak.compute.compute.application.port.out.*
import com.ttalkak.compute.compute.domain.AllocateCompute
import com.ttalkak.compute.compute.domain.ComputeRunning
import com.ttalkak.compute.compute.domain.RunningStatus
import com.ttalkak.compute.compute.domain.ServiceType
import io.github.oshai.kotlinlogging.KotlinLogging

@UseCase
class ComputeService (
    private val saveComputePort: SaveComputePort,
    private val saveDeploymentStatusPort: SaveDeploymentStatusPort,
    private val saveRunningPort: SaveRunningPort,
    private val savePortPort: SavePortPort,
    private val loadRunningPort: LoadRunningPort,
    private val removePortPort: RemovePortPort,
    private val removeDeploymentStatusPort: RemoveDeploymentStatusPort,
    private val removeRunningPort: RemoveRunningPort,
    private val deploymentFeignClient: DeploymentFeignClient
): ComputeUseCase, ComputeStatusUseCase, UpsertRunningUseCase, LoadRunningUseCase {
    val log = KotlinLogging.logger {  }

    override fun connect(command: ConnectCommand) {
        saveComputePort.saveCompute(
            userId = command.userId,
            computeType = command.computeType,
            usedCompute = command.usedCompute,
            usedMemory = command.usedMemory,
            usedCPU = command.usedCPU
        )

        savePortPort.savePort(
            userId = command.userId,
            ports = command.ports
        )
    }

    override fun disconnect(userId: Long) {
        saveComputePort.deleteCompute(userId)
        removePortPort.removePort(userId)
        removeRunningPort.removeRunningByUserId(userId)
        removeDeploymentStatusPort.removeDeploymentStatusByUserId(userId)
        loadRunningPort.loadRunningByUserId(userId).forEach {
            val status = DeploymentUpdateStatusRequest(
                id = it.id,
                serviceType = it.serviceType,
                status = RunningStatus.ERROR,
                message = "노드 서버 연결이 끊어짐"
            )

            try {
                deploymentFeignClient.updateStatus(status)
            } catch (e: Exception) {
                log.error(e) { "직접 연결: 디플로이먼트 상태 업데이트 실패" }
            }
        }
    }

    override fun update(command: StatusUpdateCommand, deploymentCommands: List<DeploymentCommand>) {
        saveComputePort.saveCompute(
            userId = command.userId,
            computeType = command.computeType,
            usedCompute = command.usedCompute,
            usedMemory = command.usedMemory,
            usedCPU = command.usedCPU
        )

        deploymentCommands.forEach {
            saveDeploymentStatusPort.saveDeploymentStatus(
                userId = command.userId,
                id = it.id,
                serviceType = it.serviceType,
                status = it.status,
                useMemory = it.useMemory,
                useCPU = it.useCPU,
                runningTime = it.runningTime,
                diskRead = it.diskRead,
                diskWrite = it.diskWrite
            )
        }
    }

    override fun upsertRunning(userId: Long, command: RunningCommand) {
        // TODO: compute Type 추가
        saveRunningPort.saveRunning(
            userId = userId,
            id = command.id,
            serviceType = command.serviceType,
            port = command.port,
            status = command.status,
            message = command.message
        )

        try {
            val request = DeploymentUpdateStatusRequest(
                id = command.id,
                serviceType = command.serviceType,
                status = command.status,
                message = command.message ?: ""
            )

            log.info {
                "배포 상태 업데이트 요청: $request"
            }

            deploymentFeignClient.updateStatus(request)
        } catch (e: Exception) {
            log.error(e) { "Deployment 상태 업데이트 실패" }
        }
    }

    override fun loadRunning(id: Long, serviceType: ServiceType): ComputeRunning {
        return loadRunningPort.loadRunning(id, serviceType)
    }
}