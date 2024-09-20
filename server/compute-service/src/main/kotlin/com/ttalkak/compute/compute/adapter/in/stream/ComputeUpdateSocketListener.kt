package com.ttalkak.compute.compute.adapter.`in`.stream

import com.ttalkak.compute.common.SocketAdapter
import com.ttalkak.compute.common.util.Json
import com.ttalkak.compute.compute.application.port.`in`.LoadRunningUseCase
import com.ttalkak.compute.compute.domain.ComputeCreateEvent
import com.ttalkak.compute.compute.domain.UpdateComputeStatusEvent
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.messaging.simp.SimpMessagingTemplate

@SocketAdapter
class ComputeUpdateSocketListener (
    private val redisTemplate: RedisTemplate<String, String>,
    private val simpleMessagingTemplate: SimpMessagingTemplate,
    private val loadRunningUseCase: LoadRunningUseCase
): MessageListener {
    override fun onMessage(message: Message, pattern: ByteArray?) {
        val response = redisTemplate.stringSerializer.deserialize(message.body)?.let {
            Json.deserialize(it, UpdateComputeStatusEvent::class.java)
        }

        require(response != null) { "컴퓨터 상태 변경 통신에 문제가 발생하였습니다." }

        val running = loadRunningUseCase.loadRunning(response.deploymentId)

        // TODO: 만약, 이미 실행중인 인스턴스에 상태 수정을 막아야한다면 여기서 처리해야됨.

        simpleMessagingTemplate.convertAndSend("/sub/compute-update/${running.userId}", Json.serialize(response))
    }
}