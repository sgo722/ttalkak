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

@SocketAdapter
class DatabaseCreateSocketListener (
    private val redisTemplate: RedisTemplate<String, String>,
    private val allocateUseCase: AllocateUseCase
): MessageListener {
    private val log = KotlinLogging.logger {}

    override fun onMessage(message: Message, pattern: ByteArray?) {
        val response = redisTemplate.stringSerializer.deserialize(message.body)?.let {
            Json.deserialize(it, DatabaseCreateEvent::class.java)
        }

        require(response != null) {
            "이벤트 수신에 문제가 발생하였습니다."
        }

        log.debug {
            "신규 데이터베이스 생성 요청: $response"
        }

        val database = response.database.databaseType.parse(
            name = response.database.name,
            username = response.database.username,
            password = response.database.password
        )

        val container = DockerDatabaseContainer(
            containerName = "${response.database.name}-${response.databaseId}-db",
            subdomainKey = response.subdomainKey,
            envs = database.envs,
            inboundPort = response.database.databaseType.port(),
            outboundPort = response.port,
            databaseId = response.databaseId,
            serviceType = ServiceType.DATABASE,
            dockerImageName = database.name,
            dockerImageTag = database.tag
        )

        val command = AddComputeCommand(
            id = response.databaseId,
            isDatabase = true,
            useMemory = 0.512,
            useCPU = 5.0,
            container = container
        )

        log.debug {
            "신규 컴퓨터 할당 요청: $command"
        }

        allocateUseCase.addQueue(command)
    }

    private fun DatabaseType.port() = when(this) {
        DatabaseType.MYSQL -> 3306
        DatabaseType.POSTGRESQL -> 5432
        DatabaseType.REDIS -> 6379
        DatabaseType.MONGODB -> 27017
        DatabaseType.MARIADB -> 3306
    }

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
                tag = "17",
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
                tag = "8.0.0",
                envs = listOf(
                    Environment("MONGO_INITDB_DATABASE", name),
                    Environment("MONGO_INITDB_ROOT_USERNAME", username),
                    Environment("MONGO_INITDB_ROOT_PASSWORD", password)
                )
            )
            DatabaseType.MARIADB -> DatabaseContainer(
                name = "mariadb",
                tag = "11.5.2",
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