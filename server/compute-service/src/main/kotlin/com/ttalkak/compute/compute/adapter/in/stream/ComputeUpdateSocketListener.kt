package com.ttalkak.compute.compute.adapter.`in`.stream

import com.ttalkak.compute.common.SocketAdapter
import com.ttalkak.compute.common.util.Json
import com.ttalkak.compute.compute.application.port.`in`.AddComputeCommand
import com.ttalkak.compute.compute.application.port.`in`.AllocateUseCase
import com.ttalkak.compute.compute.application.port.`in`.LoadRunningUseCase
import com.ttalkak.compute.compute.domain.ComputeCreateEvent
import com.ttalkak.compute.compute.domain.DockerContainer
import com.ttalkak.compute.compute.domain.UpdateComputeStatusEvent
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.messaging.simp.SimpMessagingTemplate

@SocketAdapter
class ComputeUpdateSocketListener (
    private val redisTemplate: RedisTemplate<String, String>,
    private val allocateUseCase: AllocateUseCase,
    private val loadRunningUseCase: LoadRunningUseCase
): MessageListener {
    override fun onMessage(message: Message, pattern: ByteArray?) {
        val response = redisTemplate.stringSerializer.deserialize(message.body)?.let {
            Json.deserialize(it, ComputeCreateEvent::class.java)
        }

        require(response != null) { "컴퓨터 상태 변경 통신에 문제가 발생하였습니다." }

        val running = loadRunningUseCase.loadRunning(response.deploymentId)

        // * 컴퓨터 상태 변경 이벤트 발생\
        val command = AddComputeCommand(
            id = response.deploymentId,
            isDatabase = false,
            useMemory = 0.512,
            useCPU = 5.0,
            container = DockerContainer(
                deploymentId = response.deploymentId,
                serviceType = response.serviceType,
                hasDockerImage = false,
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
        )

        allocateUseCase.addRebuildQueue(command)
    }

    private fun parseGithubLink(baseURL: String, branch: String): String = "$baseURL/archive/refs/heads/$branch.zip"
}