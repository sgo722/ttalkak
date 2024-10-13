package com.ttalkak.deployment.deployment.framework.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttalkak.deployment.deployment.application.usecase.UpdateHostingUseCase;
import com.ttalkak.deployment.deployment.domain.event.HostingEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class HostingEventConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UpdateHostingUseCase updateHostingUsecase;

    @KafkaListener(topics = "${consumers.topics.create-hosting.name}", groupId = "${consumers.group-id.save-hosting.name}")
    public void consumeHosting(ConsumerRecord<String, String> record) throws IOException {
        HostingEvent hostingEvent = objectMapper.readValue(record.value(), HostingEvent.class);
        updateHostingUsecase.updateHosting(hostingEvent);
    }
}
