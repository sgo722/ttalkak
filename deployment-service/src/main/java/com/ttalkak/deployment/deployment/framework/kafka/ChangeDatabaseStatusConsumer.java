package com.ttalkak.deployment.deployment.framework.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttalkak.deployment.deployment.application.usecase.UpdateDatabaseStatusUseCase;
import com.ttalkak.deployment.deployment.framework.web.request.DatabaseUpdateStatusRequest;
import com.ttalkak.deployment.deployment.framework.web.request.UpdateStatusRequest;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class ChangeDatabaseStatusConsumer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UpdateDatabaseStatusUseCase updateDatabaseStatusUseCase;

    @KafkaListener(topics = "${consumers.topics.update-database-status.name}", groupId = "${consumers.group-id.update-database-status.name}")
    public void deleteConsumer(ConsumerRecord<String, String> record) throws IOException {
        UpdateStatusRequest updateStatusRequest = objectMapper.readValue(record.value(), UpdateStatusRequest.class);
        updateDatabaseStatusUseCase.updateDatabaseStatus(updateStatusRequest);
    }
}
