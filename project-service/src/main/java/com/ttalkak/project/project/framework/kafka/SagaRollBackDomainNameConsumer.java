package com.ttalkak.project.project.framework.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttalkak.project.project.application.usecase.UpdateProjectUseCase;
import com.ttalkak.project.project.domain.event.DomainNameEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SagaRollBackDomainNameConsumer {

    private final ObjectMapper objectMapper;

    private final UpdateProjectUseCase updateProjectUseCase;

    @KafkaListener(topics = "${consumers.topics.update-domain-name-exception.name}", groupId = "${consumers.group-id.update-hosting-status.name}")
    public void consumeRollBack(ConsumerRecord<String, String> record) throws IOException {
        log.info("consume roll back:{}", record.value());
        DomainNameEvent domainNameEvent = objectMapper.readValue(record.value(), DomainNameEvent.class);
        updateProjectUseCase.rollbackProjectDomainName(domainNameEvent);
    }

}
