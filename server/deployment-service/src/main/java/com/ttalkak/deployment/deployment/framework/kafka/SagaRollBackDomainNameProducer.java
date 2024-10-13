package com.ttalkak.deployment.deployment.framework.kafka;

import com.ttalkak.deployment.common.global.error.ErrorCode;
import com.ttalkak.deployment.common.global.exception.BusinessException;
import com.ttalkak.deployment.deployment.domain.event.DomainNameEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SagaRollBackDomainNameProducer {

    @Value("${producers.topics.update-domain-name-exception.name}")
    private String TOPIC_ROLLBACK_INSTANCE;

    private final KafkaTemplate<String, DomainNameEvent> kafkaTemplate;

    // 도메인명 롤백
    public void rollbackDomainName(DomainNameEvent domainNameEvent) {
        CompletableFuture<SendResult<String, DomainNameEvent>> future = kafkaTemplate.send(TOPIC_ROLLBACK_INSTANCE, domainNameEvent);

        future.thenAccept(sendResult -> {
            log.info("롤백 - 프로젝트 ID {} 도메인명 {} 변경", domainNameEvent.getProjectId(), domainNameEvent.getNewDomainName());
        }).exceptionally(ex ->{
            throw new BusinessException(ErrorCode.KAFKA_DOMAIN_PRODUCER_ERROR);
        });


    }
}
