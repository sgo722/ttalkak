package com.ttalkak.compute.compute.application.service

import com.ttalkak.compute.common.UseCase
import com.ttalkak.compute.common.util.Json
import com.ttalkak.compute.compute.adapter.out.feign.DeploymentFeignClient
import com.ttalkak.compute.compute.adapter.out.feign.request.DeploymentUpdateStatusRequest
import com.ttalkak.compute.compute.application.port.`in`.AddComputeCommand
import com.ttalkak.compute.compute.application.port.`in`.AllocateUseCase
import com.ttalkak.compute.compute.application.port.out.*
import com.ttalkak.compute.compute.domain.*
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled

@UseCase
class AllocateService (
    private val createAllocatePort: CreateAllocatePort,
    private val loadAllocatePort: LoadAllocatePort,
    private val loadStatusPort: LoadStatusPort,
    private val loadPortPort: LoadPortPort,
    private val savePortPort: SavePortPort,
    private val deploymentFeignClient: DeploymentFeignClient,
    private val simpleMessagingTemplate: SimpMessagingTemplate,
    private val loadComputePort: LoadComputePort,
    private val redisLockPort: RedisLockPort
): AllocateUseCase {
    companion object {
        const val ALLOCATE_LOCK_KEY = "computeAllocateLock"
    }

    private val log = KotlinLogging.logger {  }

    override fun addQueue(command: AddComputeCommand) {
        log.info {
            "신규 컴퓨터 할당 요청: $command"
        }

        createAllocatePort.append(
            id = command.id,
            isDatabase = command.isDatabase,
            useMemory = command.useMemory,
            useCPU = command.useCPU,
            instance = command.container
        )

        if (!command.isDatabase) {
            deploymentFeignClient.updateStatus(DeploymentUpdateStatusRequest(
                deploymentId = command.id,
                status = RunningStatus.WAITING,
                message = "컴퓨터 할당 대기중"
            ))
        }
    }

    override fun addRebuildQueue(command: AddComputeCommand) {
        log.info {
            "재할당 요청: $command"
        }

        createAllocatePort.appendPriority(
            id = command.id,
            isDatabase = command.isDatabase,
            rebuild = true,
            useMemory = command.useMemory,
            useCPU = command.useCPU,
            instance = command.container
        )

        deploymentFeignClient.updateStatus(DeploymentUpdateStatusRequest(
            deploymentId = command.id,
            status = RunningStatus.WAITING,
            message = "컴퓨터 재할당 대기중"
        ))
    }

    @Scheduled(fixedDelay = 1000 * 60)
    fun process() {
        if (!redisLockPort.lock(ALLOCATE_LOCK_KEY, 1000 * 60)) {
            log.error { "이미 이전 프로세스가 실행중입니다." }
            return
        }

        log.debug { "컴퓨터 할당 프로세스 시작" }

        val tries = loadAllocatePort.findDeploymentIds().associateWith { false }.toMutableMap()

        while (loadAllocatePort.size() > 0) {
            // * 할당할 컴퓨터 선정
            val compute = loadAllocatePort.findFirst()

            // * 할당 로직
            val availableCompute = loadComputePort.loadAllCompute().filter {
                it.isAvailable(compute.useMemory, compute.useCPU)
            }.maxByOrNull {
                it.weight
            }

            // * 할당 가능한 컴퓨터 실패 로직
            if (availableCompute == null) {
                if (tries[compute.id] == false) {
                    log.error { "할당 가능한 컴퓨터가 없습니다." }
                    loadAllocatePort.pop().ifPresent {
                        createAllocatePort.append(
                            id = it.id,
                            isDatabase = it.isDatabase,
                            useMemory = it.useMemory,
                            useCPU = it.useCPU,
                            instance = it.instance
                        )
                    }
                    tries[compute.id] = true
                    continue
                } else {
                    log.error { "할당 가능한 컴퓨터가 없습니다. 재시도를 중단합니다." }
                    break
                }
            } else {
                loadAllocatePort.pop()
            }

            // * 컴퓨터에 할당 가능한 포트 저장
            val availablePorts = loadStatusPort.loadStatus(availableCompute.userId).orElseThrow {
                IllegalArgumentException("유저에 알맞는 상태가 존재하지 않습니다.")
            }.let { status ->
                status.availablePortStart..status.availablePortEnd
            }.subtract(loadPortPort.loadPorts(availableCompute.userId).toSet())


            // * 포트 할당
            val randomPort = availablePorts.random()

            if (compute.isDatabase) {
                val instance = compute.instance as DockerDatabaseContainer
                instance.outboundPort = randomPort
            } else {
                val instance = compute.instance as DockerContainer
                instance.outboundPort = randomPort
            }

            savePortPort.savePort(availableCompute.userId, randomPort)

            simpleMessagingTemplate.convertAndSend("/sub/compute-create/${availableCompute.userId}", Json.serialize(compute.instance))
        }

        redisLockPort.unlock(ALLOCATE_LOCK_KEY)
    }
}