package com.ttalkak.compute.compute.application.service

import com.ttalkak.compute.common.UseCase
import com.ttalkak.compute.compute.adapter.out.feign.DeploymentFeignClient
import com.ttalkak.compute.compute.adapter.out.feign.request.DeploymentUpdateStatusRequest
import com.ttalkak.compute.compute.application.port.`in`.*
import com.ttalkak.compute.compute.application.port.out.*
import com.ttalkak.compute.compute.domain.AllocateCompute
import com.ttalkak.compute.compute.domain.ComputeRunning
import io.github.oshai.kotlinlogging.KotlinLogging

@UseCase
class ComputeService (
    private val saveComputePort: SaveComputePort,
    private val saveDeploymentStatusPort: SaveDeploymentStatusPort,
    private val saveRunningPort: SaveRunningPort,
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
    }

    override fun disconnect(userId: Long) {
        saveComputePort.deleteCompute(userId)
        removePortPort.removePort(userId)
        removeRunningPort.removeRunningByUserId(userId)
        removeDeploymentStatusPort.removeDeploymentStatusByUserId(userId)
    }

    override fun update(connectCommand: ConnectCommand, deploymentCommands: List<DeploymentCommand>) {
        connect(connectCommand)

        deploymentCommands.forEach {
            saveDeploymentStatusPort.saveDeploymentStatus(
                userId = connectCommand.userId,
                deploymentId = it.deploymentId,
                status = it.status,
                useMemory = it.useMemory,
                useCPU = it.useCPU,
                runningTime = it.runningTime,
                diskRead = it.diskRead,
                diskWrite = it.diskWrite
            )
        }
    }

    override fun upsertRunning(userId: Long, runningCommand: RunningCommand) {
        saveRunningPort.saveRunning(
            userId = userId,
            deploymentId = runningCommand.deploymentId,
            port = runningCommand.port,
            status = runningCommand.status,
            message = runningCommand.message
        )

        try {
            val request = DeploymentUpdateStatusRequest(
                deploymentId = runningCommand.deploymentId,
                status = runningCommand.status,
                message = runningCommand.message ?: ""
            )

            log.info {
                "배포 상태 업데이트 요청: $request"
            }

            deploymentFeignClient.updateStatus(request)
        } catch (e: Exception) {
            log.error(e) { "Deployment 상태 업데이트 실패" }
        }
    }

    override fun loadRunning(deploymentId: Long): ComputeRunning {
        return loadRunningPort.loadRunning(deploymentId)
    }
}