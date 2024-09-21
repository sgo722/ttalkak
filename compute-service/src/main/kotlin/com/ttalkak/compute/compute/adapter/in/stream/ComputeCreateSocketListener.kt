package com.ttalkak.compute.compute.adapter.`in`.stream

import com.ttalkak.compute.common.SocketAdapter
import com.ttalkak.compute.common.StreamAdapter
import com.ttalkak.compute.common.util.Json
import com.ttalkak.compute.compute.application.port.`in`.AllocateCommand
import com.ttalkak.compute.compute.application.port.`in`.AllocateUseCase
import com.ttalkak.compute.compute.domain.ComputeCreateEvent
import com.ttalkak.compute.compute.domain.DockerContainer
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.messaging.simp.SimpMessagingTemplate

@SocketAdapter
class ComputeCreateSocketListener(
    private val redisTemplate: RedisTemplate<String, String>,
    private val simpleMessagingTemplate: SimpMessagingTemplate,
    private val allocateUseCase: AllocateUseCase
): MessageListener {

    override fun onMessage(message: Message, pattern: ByteArray?) {
        val response = redisTemplate.stringSerializer.deserialize(message.body)?.let {
            Json.deserialize(it, ComputeCreateEvent::class.java)
        }

        require(response != null) { "이벤트 수신에 문제가 발생하였습니다." }

//        val command = AllocateCommand(
//            computeCount = response.databases.size + 1,
//            useMemory = 0,
//            usePorts = response.databases.map { it.port } + response.port
//        )

//        val deployerId = allocateUseCase.allocate(command)
        val deployerId = 2L

        val mainContainer = DockerContainer(
            deploymentId = response.deploymentId,
            hasDockerImage = false,
            containerName = "${response.serviceType}-${response.deploymentId}",
            inboundPort = response.port,
            outboundPort = response.port,
            subdomainName = response.subdomainName,
            subdomainKey = response.subdomainKey,
            sourceCodeLink = parseGithubLink(response.repositoryUrl, response.branch),
            dockerRootDirectory = response.rootDirectory,
            dockerImageName = null,
            dockerImageTag = null
        )
        simpleMessagingTemplate.convertAndSend("/sub/compute-create/${deployerId}", Json.serialize(listOf(mainContainer)))
    }

    private fun parseGithubLink(baseURL: String, branch: String): String = "$baseURL/archive/refs/heads/$branch.zip"
}