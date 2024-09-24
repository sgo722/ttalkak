package com.ttalkak.deployment.deployment.framework.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttalkak.deployment.deployment.application.usecase.UpdateDeploymentStatusUsecase;
import com.ttalkak.deployment.deployment.framework.web.request.DeploymentUpdateStatusRequest;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class ChangeStatusConsumer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UpdateDeploymentStatusUsecase updateDeploymentStatusUsecase;

    @KafkaListener(topics = "${consumers.topics.update-deployment-status.name}", groupId = "${consumers.group-id.update-deployment-status.name}")
    public void deleteConsumer(ConsumerRecord<String, String> record) throws IOException {
        DeploymentUpdateStatusRequest deploymentUpdateStatusRequest = objectMapper.readValue(record.value(), DeploymentUpdateStatusRequest.class);
        updateDeploymentStatusUsecase.updateDeploymentStatus(deploymentUpdateStatusRequest);
    }
}
