package com.ttalkak.deployment.deployment.framework.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttalkak.deployment.deployment.application.inputport.UpdateHostingInputPort;
import com.ttalkak.deployment.deployment.domain.event.DomainNameEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HostingDomainNameEventConsumer {

    private final ObjectMapper objectMapper;

    private final UpdateHostingInputPort updateHostingInputPort;

    private final SagaRollBackDomainNameProducer sagaRollBackDomainNameProducer;

    @KafkaListener(topics = "${consumers.topics.update-domain-name.name}", groupId = "${consumers.group-id.update-hosting-status.name}")
    public void updateConsumer(ConsumerRecord<String, String> record) {
        DomainNameEvent domainNameEvent = null;
        try {
            domainNameEvent = objectMapper.readValue(record.value(), DomainNameEvent.class);
            // 호스팅 도메인명 변경
            updateHostingInputPort.updateHostingDomainName(domainNameEvent.getProjectId(), domainNameEvent.getNewDomainName());
        } catch (Exception e) {
            sagaRollBackDomainNameProducer.rollbackDomainName(domainNameEvent);
        }
    }
}
