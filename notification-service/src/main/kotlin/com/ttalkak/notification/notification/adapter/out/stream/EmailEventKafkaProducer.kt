package com.ttalkak.notification.notification.adapter.out.stream

import com.ttalkak.compute.common.StreamAdapter
import com.ttalkak.notification.common.util.Json
import com.ttalkak.notification.notification.application.port.out.VerifyEmailEventPort
import com.ttalkak.notification.notification.domain.EmailConfirmEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate

@StreamAdapter
class EmailEventKafkaProducer (
    private val kafkaTemplate: KafkaTemplate<String, EmailConfirmEvent>
): VerifyEmailEventPort {
    @Value("\${producer.topics.verify-user-email.name}")
    private lateinit var verifyUserEmailTopic: String

    private val log = KotlinLogging.logger { }

    override fun verifyEmail(event: EmailConfirmEvent) {
        log.info {
            "이메일 인증 성공 완료 후 유저 서비스에 이메일 인증 성공 요청 전송: ${event.email}"
        }

        kafkaTemplate.send(verifyUserEmailTopic, event).thenAccept {
            log.info { "이메일 인증 성공 요청 전송 성공: ${event.email}" }
        }.exceptionally {
            log.error(it) { "이메일 인증 성공 요청 전송 실패: ${event.email}" }
            null
        }
    }
}