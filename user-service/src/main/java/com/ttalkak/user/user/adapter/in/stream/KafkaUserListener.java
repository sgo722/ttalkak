package com.ttalkak.user.user.adapter.in.stream;

import com.ttalkak.user.common.StreamAdapter;
import com.ttalkak.user.common.util.Json;
import com.ttalkak.user.user.adapter.in.stream.request.EmailVerifyRequest;
import com.ttalkak.user.user.application.port.in.UserEmailVerifyUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

@Slf4j
@StreamAdapter
@RequiredArgsConstructor
public class KafkaUserListener {
    private final UserEmailVerifyUseCase userEmailVerifyUseCase;

    @KafkaListener(topics = {"${consumer.topics.verify-user-email.name}"}, groupId = "${spring.kafka.consumer.group-id}")
    public void listen(@Payload String record) {
        EmailVerifyRequest request = Json.deserialize(record, EmailVerifyRequest.class);

        log.info("유저 이메일 인증 로직 실행 - 발송 이메일 : {}", request.getEmail());

        userEmailVerifyUseCase.verifyEmail(request.getUserId(), request.getEmail());
    }
}
