package com.ttalkak.deployment.deployment.framework.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttalkak.deployment.deployment.application.inputport.DeleteDeploymentInputPort;
import com.ttalkak.deployment.deployment.domain.event.DeleteDeploymentsEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteEventConsumer {
    private final ObjectMapper objectMapper;

    private final DeleteDeploymentInputPort deleteDeploymentInputPort;

    private final SagaRollBackProducer sagaRollBackProducer;

    @KafkaListener(topics = "${consumers.topics.delete-deployment.name}", groupId = "${consumers.group-id.delete-deployment.name}")
    public void deleteConsumer(ConsumerRecord<String, String> record) {
        DeleteDeploymentsEvent deleteDeploymentsEvent = null;
        try {
            deleteDeploymentsEvent = objectMapper.readValue(record.value(), DeleteDeploymentsEvent.class);
            deleteDeploymentInputPort.deleteDeploymentByProject(deleteDeploymentsEvent.getProjectId());
        } catch (Exception e) {
            sagaRollBackProducer.rollbackDeleteStatus(deleteDeploymentsEvent);
        }
    }
}
