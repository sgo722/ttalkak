package com.ttalkak.deployment.deployment.framework.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttalkak.deployment.common.global.error.ErrorCode;
import com.ttalkak.deployment.common.global.exception.BusinessException;
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
public class ChangeDeploymentStatusProducer {
    @Value("${producers.topics.command-deployment-status.name}")
    private String TOPIC_CREATE_INSTANCE;

    private final KafkaTemplate<String, UpdateDeploymentStatusEvent> kafkaTemplate;

    private final Logger LOGGER = LoggerFactory.getLogger(DeploymentKafkaProducer.class);

    public void occurUpdateDeploymentStatus(UpdateDeploymentStatusEvent updateDeploymentStatusEvent) throws JsonProcessingException {

        CompletableFuture<SendResult<String, UpdateDeploymentStatusEvent>> future = kafkaTemplate.send(TOPIC_CREATE_INSTANCE, updateDeploymentStatusEvent);
        // 콜백 메서드 생성 해야함.
        future.thenAccept(result -> {
            UpdateDeploymentStatusEvent value = result.getProducerRecord().value();
            LOGGER.info("Sent message=[" + value.getId() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
        }).exceptionally(ex ->{
            throw new BusinessException(ErrorCode.KAFKA_CHANGE_DEPLOYMENT_STATUS_PRODUCER_ERROR);
        });
    }
}
