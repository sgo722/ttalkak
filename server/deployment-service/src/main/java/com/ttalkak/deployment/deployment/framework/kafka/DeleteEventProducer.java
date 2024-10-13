package com.ttalkak.deployment.deployment.framework.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttalkak.deployment.common.global.error.ErrorCode;
import com.ttalkak.deployment.common.global.exception.BusinessException;
import com.ttalkak.deployment.deployment.application.outputport.EventOutputPort;
import com.ttalkak.deployment.deployment.domain.event.CreateDatabaseEvent;
import com.ttalkak.deployment.deployment.domain.event.CreateInstanceEvent;
import com.ttalkak.deployment.deployment.domain.event.DeleteDatabaseEvent;
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
public class DeleteEventProducer{

    @Value("${producers.topics.delete-database.name}")
    private String TOPIC_DELETE_DATABASE;

    private final KafkaTemplate<String, DeleteDatabaseEvent> kafkaTemplate;

    private final Logger LOGGER = LoggerFactory.getLogger(DeleteEventProducer.class);

    public void occurDeleteDatabase(DeleteDatabaseEvent deleteDatabaseEvent) throws JsonProcessingException {
        CompletableFuture<SendResult<String, DeleteDatabaseEvent>> future = kafkaTemplate.send(TOPIC_DELETE_DATABASE, deleteDatabaseEvent);
        // 콜백 메서드 생성 해야함.
        future.thenAccept(result -> {
            DeleteDatabaseEvent value = result.getProducerRecord().value();
            LOGGER.info("Sent message=[" + value.getDatabaseId() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
        }).exceptionally(ex ->{
            throw new BusinessException(ErrorCode.KAFKA_CHANGE_DATABASE_STATUS_PRODUCER_ERROR);
        });
    }


}
