package com.ttalkak.deployment.deployment.framework.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttalkak.deployment.deployment.domain.event.UpdateDatabaseStatusEvent;
import com.ttalkak.deployment.deployment.domain.event.UpdateDeploymentStatusEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ChangeDatabaseStatusProducer {
    @Value("${producers.topics.command-deployment-status.name}")
    private String TOPIC_COMMAND_DATABASE_STATUS;

    private final KafkaTemplate<String, UpdateDatabaseStatusEvent> kafkaTemplate;

    private final Logger LOGGER = LoggerFactory.getLogger(DeploymentKafkaProducer.class);

    public void occurUpdateDatabaseStatus(UpdateDatabaseStatusEvent updateDatabaseStatusEvent) throws JsonProcessingException {

        CompletableFuture<SendResult<String, UpdateDatabaseStatusEvent>> future = kafkaTemplate.send(TOPIC_COMMAND_DATABASE_STATUS, updateDatabaseStatusEvent);
        // 콜백 메서드 생성 해야함.
        future.thenAccept(result -> {
            UpdateDatabaseStatusEvent value = result.getProducerRecord().value();
            LOGGER.info("Sent message=[" + value.getId() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
        }).exceptionally(ex ->{
            throw new IllegalArgumentException(ex);
        });
    }
}
