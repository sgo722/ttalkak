package com.ttalkak.compute.compute.adapter.`in`.stream

import com.ttalkak.compute.common.SocketAdapter
import com.ttalkak.compute.common.util.Json
import com.ttalkak.compute.compute.application.port.`in`.AddComputeCommand
import com.ttalkak.compute.compute.application.port.`in`.AllocateUseCase
import com.ttalkak.compute.compute.domain.*
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.messaging.simp.SimpMessagingTemplate

@SocketAdapter
class ComputeCreateSocketListener(
    private val redisTemplate: RedisTemplate<String, String>,
    private val allocateUseCase: AllocateUseCase
): MessageListener {
    private val log = KotlinLogging.logger {}

    override fun onMessage(message: Message, pattern: ByteArray?) {
        val response = redisTemplate.stringSerializer.deserialize(message.body)?.let {
            Json.deserialize(it, ComputeCreateEvent::class.java)
        }

        require(response != null) {
            "이벤트 수신에 문제가 발생하였습니다."
        }

        log.debug {
            "신규 컴퓨터 생성 요청: $response (rebuild: ${response.isRebuild})"
        }

        val container = DockerContainer(
            deploymentId = response.deploymentId,
            serviceType = response.serviceType,
            containerName = "${response.serviceType}-${response.deploymentId}",
            inboundPort = response.port,
            subdomainName = response.subdomainName,
            subdomainKey = response.subdomainKey,
            sourceCodeLink = parseGithubLink(response.repositoryUrl, response.branch),
            dockerRootDirectory = response.rootDirectory,
            hasDockerFile = response.dockerfileExist,
            dockerFileScript = response.dockerfileScript,
            envs = response.envs
        )

        val command = AddComputeCommand(
            id = response.deploymentId,
            isDatabase = false,
            useMemory = 0.512,
            useCPU = 5.0,
            senderId = response.senderId,
            container = container
        )

        log.debug {
            "신규 컴퓨터 할당 요청: $command (rebuild: ${response.isRebuild})"
        }

        if (response.isRebuild!!) {
            allocateUseCase.addRebuildQueue(command)
        } else {
            allocateUseCase.addQueue(command)
        }
    }

    private fun parseGithubLink(baseURL: String, branch: String): String = "$baseURL/archive/refs/heads/$branch.zip"
}