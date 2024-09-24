package com.ttalkak.deployment.deployment.framework.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttalkak.deployment.deployment.application.outputport.EventOutputPort;
import com.ttalkak.deployment.deployment.domain.event.CreateInstanceEvent;
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
public class DeploymentKafkaProducer implements EventOutputPort {

    @Value("${producers.topics.create-compute.name}")
    private String TOPIC_CREATE_INSTANCE;

    @Value("${producers.topics.rebuild-compute.name}")
    private String TOPIC_REBUILD_INSTANCE;

    private final KafkaTemplate<String, CreateInstanceEvent> kafkaTemplate;

    private final Logger LOGGER = LoggerFactory.getLogger(DeploymentKafkaProducer.class);

    @Override
    public void occurCreateInstance(CreateInstanceEvent createInstanceEvent) throws JsonProcessingException {

        CompletableFuture<SendResult<String, CreateInstanceEvent>> future = kafkaTemplate.send(TOPIC_CREATE_INSTANCE, createInstanceEvent);
        // 콜백 메서드 생성 해야함.
        future.thenAccept(result -> {
            CreateInstanceEvent value = result.getProducerRecord().value();
            LOGGER.info("Sent message=[" + value.getDeploymentId() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
        }).exceptionally(ex ->{
            throw new IllegalArgumentException(ex);
        });
    }

    @Override
    public void occurRebuildInstance(CreateInstanceEvent createInstanceEvent) throws JsonProcessingException {
        CompletableFuture<SendResult<String, CreateInstanceEvent>> future = kafkaTemplate.send(TOPIC_REBUILD_INSTANCE, createInstanceEvent);
        // 콜백 메서드 생성 해야함.
        future.thenAccept(result -> {
            CreateInstanceEvent value = result.getProducerRecord().value();
            LOGGER.info("Sent message=[" + value.getDeploymentId() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
        }).exceptionally(ex ->{
            throw new IllegalArgumentException(ex);
        });
    }
}
