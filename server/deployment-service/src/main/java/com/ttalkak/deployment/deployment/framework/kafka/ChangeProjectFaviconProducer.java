package com.ttalkak.deployment.deployment.framework.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttalkak.deployment.common.global.error.ErrorCode;
import com.ttalkak.deployment.common.global.exception.BusinessException;
import com.ttalkak.deployment.deployment.domain.event.UpdateDeploymentStatusEvent;
import com.ttalkak.deployment.deployment.domain.event.UpdateFaviconEvent;
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
public class ChangeProjectFaviconProducer {
    @Value("${producers.topics.update-project-favicon.name}")
    private String TOPIC_UPDATE_FAVICON;

    private final KafkaTemplate<String, UpdateFaviconEvent> kafkaTemplate;

    private final Logger LOGGER = LoggerFactory.getLogger(DeploymentKafkaProducer.class);

    public void updateFaviconEvent(UpdateFaviconEvent updateFaviconEvent) throws JsonProcessingException {
        CompletableFuture<SendResult<String, UpdateFaviconEvent>> future = kafkaTemplate.send(TOPIC_UPDATE_FAVICON, updateFaviconEvent);
        // 콜백 메서드 생성 해야함.
        future.thenAccept(result -> {
            UpdateFaviconEvent value = result.getProducerRecord().value();
            LOGGER.info("Sent message=[" + value.getProjectId() + "]");
        }).exceptionally(ex ->{
            throw new BusinessException(ErrorCode.KAFKA_CHANGE_PAVICON_STATUS_PRODUCER_ERROR);
        });
    }
}