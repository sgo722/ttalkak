package com.ttalkak.compute.compute.adapter.`in`.stream

import com.ttalkak.compute.common.StreamAdapter
import com.ttalkak.compute.common.util.Json
import com.ttalkak.compute.compute.application.port.`in`.StatusCommand
import com.ttalkak.compute.compute.application.port.`in`.UpdateStatusUseCase
import com.ttalkak.compute.compute.domain.ComputeCreateEvent
import com.ttalkak.compute.compute.domain.RunningCommand
import com.ttalkak.compute.compute.domain.UpdateComputeStatusEvent
import com.ttalkak.compute.compute.domain.UserCreateEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload

@StreamAdapter
class KafkaComputeListener(
    private val redisTemplate: RedisTemplate<String, String>,
    private val redisMessageListenerContainer: RedisMessageListenerContainer,
    private val computeCreateSocketListener: ComputeCreateSocketListener,
    private val computeUpdateSocketListener: ComputeUpdateSocketListener,
    private val updateStatusUseCase: UpdateStatusUseCase
) {
    private val log = KotlinLogging.logger {}
    private val computeCreateChannel = ChannelTopic("compute-create")
    private val computeUpdateChannel = ChannelTopic("compute-update")

    @PostConstruct
    fun init() {
        redisMessageListenerContainer.addMessageListener(computeCreateSocketListener, computeCreateChannel)
        redisMessageListenerContainer.addMessageListener(computeUpdateSocketListener, computeUpdateChannel)
    }

    @KafkaListener(topics = ["\${consumer.topics.create-compute.name}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun createCompute(@Payload record: String) {
        val response = Json.deserialize(record, ComputeCreateEvent::class.java)
        log.info {
            "컴퓨터 생성 이벤트 발생: ${response.deploymentId}"
        }
        redisTemplate.convertAndSend(computeCreateChannel.topic, Json.serialize(response))
    }

    @KafkaListener(topics = ["\${consumer.topics.create-user.name}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun createUser(@Payload record: String) {
        val response = Json.deserialize(record, UserCreateEvent::class.java)
        log.info {
            "유저 생성 이벤트 발생: ${response.userId}"
        }

        // * 신규 유저 생성 시 초기 데이터 생성
        val command = StatusCommand(
            maxCompute = 5,
            availablePortStart = 10000,
            availablePortEnd = 15000,
            maxMemory = 16.0,
            maxCPU = 8.0
        )

        updateStatusUseCase.upsertStatus(response.userId, command)
    }

    @KafkaListener(topics = ["\${consumer.topics.command-deployment-status.name}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun updateComputeStatus(@Payload record: String) {
        val response = Json.deserialize(record, UpdateComputeStatusEvent::class.java)

        log.info {
            "컴퓨터 상태 변경 이벤트 발생: ${response.deploymentId} - ${response.command}"
        }

        redisTemplate.convertAndSend(computeUpdateChannel.topic, Json.serialize(response))
    }

    @KafkaListener(topics = ["\${consumer.topics.rebuild-compute.name}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun rebuildCompute(@Payload record: String) {
        val response = Json.deserialize(record, ComputeCreateEvent::class.java)
        log.info {
            "컴퓨터 재배포 이벤트 발생: ${response.deploymentId}"
        }

        val event = UpdateComputeStatusEvent(
            deploymentId = response.deploymentId,
            command = RunningCommand.REBUILD
        )

        redisTemplate.convertAndSend(computeUpdateChannel.topic, Json.serialize(event))
    }
}