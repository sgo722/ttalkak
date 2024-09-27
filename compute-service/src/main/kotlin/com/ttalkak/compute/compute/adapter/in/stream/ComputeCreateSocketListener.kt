package com.ttalkak.compute.compute.adapter.`in`.stream

import com.ttalkak.compute.common.SocketAdapter
import com.ttalkak.compute.common.StreamAdapter
import com.ttalkak.compute.common.util.Json
import com.ttalkak.compute.compute.application.port.`in`.AllocateCommand
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
    private val simpleMessagingTemplate: SimpMessagingTemplate,
    private val allocateUseCase: AllocateUseCase
): MessageListener {
    private val log = KotlinLogging.logger {}

    override fun onMessage(message: Message, pattern: ByteArray?) {
        val response = redisTemplate.stringSerializer.deserialize(message.body)?.let {
            Json.deserialize(it, ComputeCreateEvent::class.java)
        }

        require(response != null) { "이벤트 수신에 문제가 발생하였습니다." }

        val command = AllocateCommand(
            computeCount = response.databases.size + 1,
            useMemory = 0.512 * (response.databases.size + 1)
        )

        val allocate = allocateUseCase.allocate(command)
        log.info {
            "배포 요청: $response, 할당 결과: $allocate"
        }

        val deployerId = allocate.userId

        val mainContainer = DockerContainer(
            deploymentId = response.deploymentId,
            hasDockerImage = false,
            containerName = "${response.serviceType}-${response.deploymentId}",
            inboundPort = response.port,
            outboundPort = allocate.ports.first(),
            subdomainName = response.subdomainName,
            subdomainKey = response.subdomainKey,
            sourceCodeLink = parseGithubLink(response.repositoryUrl, response.branch),
            dockerRootDirectory = response.rootDirectory,
            dockerImageName = null,
            dockerImageTag = null,
            hasDockerFile = response.dockerfileExist,
            dockerFileScript = response.dockerfileScript,
            envs = response.envs
        )

        val databases = response.databases.map {
            val database = it.databaseType.parse(it.name, it.username, it.password)
            DockerContainer(
                deploymentId = response.deploymentId,
                hasDockerImage = true,
                containerName = "${response.serviceType}-${response.deploymentId}-db-${it.databaseId}",
                inboundPort = it.port,
                outboundPort = allocate.ports[response.databases.indexOf(it) + 1],
                hasDockerFile = false,
                dockerImageName = database.name,
                dockerImageTag = database.tag,
                envs = database.envs
            )
        }

        simpleMessagingTemplate.convertAndSend("/sub/compute-create/${deployerId}", Json.serialize(listOf(mainContainer) + databases))
    }

    private fun parseGithubLink(baseURL: String, branch: String): String = "$baseURL/archive/refs/heads/$branch.zip"

    private fun DatabaseType.parse(
        name: String,
        username: String,
        password: String,
    ): DatabaseContainer {
        return when(this) {
            DatabaseType.MYSQL -> DatabaseContainer(
                name = "mysql",
                tag = "5.7",
                envs = listOf(
                    Environment("MYSQL_ROOT_PASSWORD", password),
                    Environment("MYSQL_DATABASE", name),
                    Environment("MYSQL_USER", username),
                    Environment("MYSQL_PASSWORD", password)
                )
            )
            DatabaseType.POSTGRESQL -> DatabaseContainer(
                name = "postgres",
                tag = "13",
                envs = listOf(
                    Environment("POSTGRES_DB", name),
                    Environment("POSTGRES_USER", username),
                    Environment("POSTGRES_PASSWORD", password)
                )
            )
            DatabaseType.REDIS -> DatabaseContainer(
                name = "redis",
                tag = "6.2",
                envs = listOf()
            )
            DatabaseType.MONGODB -> DatabaseContainer(
                name = "mongo",
                tag = "4.4",
                envs = listOf(
                    Environment("MONGO_INITDB_DATABASE", name),
                    Environment("MONGO_INITDB_ROOT_USERNAME", username),
                    Environment("MONGO_INITDB_ROOT_PASSWORD", password)
                )
            )
            DatabaseType.MARIADB -> DatabaseContainer(
                name = "mariadb",
                tag = "10.5",
                envs = listOf(
                    Environment("MYSQL_ROOT_PASSWORD", password),
                    Environment("MYSQL_DATABASE", name),
                    Environment("MYSQL_USER", username),
                    Environment("MYSQL_PASSWORD", password)
                )
            )
        }
    }
}