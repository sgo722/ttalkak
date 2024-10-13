package com.ttalkak.project.project.framework.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttalkak.project.common.error.ErrorCode;
import com.ttalkak.project.common.exception.BusinessException;
import com.ttalkak.project.project.application.outputport.EventOutputPort;
import com.ttalkak.project.project.domain.event.DomainNameEvent;
import com.ttalkak.project.project.domain.event.ProjectEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectKafkaProducer implements EventOutputPort {

    @Value("${producers.topics.delete-deployment.name}")
    private String TOPIC_DELETE_PROJECT;

    @Value("${producers.topics.update-domain-name.name}")
    private String TOPIC_UPDATE_DOMAIN_NAME;

    private final KafkaTemplate<String, ProjectEvent> kafkaTemplateDeleteDeployment;

    private final KafkaTemplate<String, DomainNameEvent> kafkaTemplateUpdateDomainName;

    /**
     * CompletableFuture 란 자바의 동시성 프로그래밍을 위한 클래스로
     * 비동기 프로그래밍을 더 쉽고 효율적으로 만들어주는 기능을 제공한다.
     * 비동기 작업, 예외 처리, 콜백 지원, 스레드 풀(ForkJoinPool)을 사용
     */

    /**
     * Deployment service에  배포 엔티티 삭제를 요청
     * @param projectEvent
     * @throws JsonProcessingException
     */
    @Override
    public void occurDeleteDeploymentInstance(ProjectEvent projectEvent) throws JsonProcessingException {
        CompletableFuture<SendResult<String, ProjectEvent>> future = kafkaTemplateDeleteDeployment.send(TOPIC_DELETE_PROJECT, projectEvent);
        future.thenAccept(result -> {
            String value = String.valueOf(result.getProducerRecord().value().getProjectId());
            log.info("프로젝트 ID가 {}인 배포 정보를 삭제했습니다. offset=[{}]", value, result.getRecordMetadata().offset());
        }).exceptionally(ex ->{
            throw new BusinessException(ErrorCode.KAFKA_CHANGE_DEPLOYMENT_STATUS_PRODUCER_ERROR);
        });
    }

    /**
     * Deployment service에 호스팅 엔티티 도메인명 변경 요청
     * @param domainNameEvent
     */
    @Override
    public void occurUpdateHostingDomainName(DomainNameEvent domainNameEvent) {
        CompletableFuture<SendResult<String, DomainNameEvent>> future = kafkaTemplateUpdateDomainName.send(TOPIC_UPDATE_DOMAIN_NAME, domainNameEvent);
        future.thenAccept(result -> {
            String value = String.valueOf(result.getProducerRecord().value().getProjectId());
            log.info("프로젝트 ID가 {}인 호스팅 도메인명을 변경했습니다. offset=[{}]", value, result.getRecordMetadata().offset());
        }).exceptionally(ex -> {
            throw new BusinessException(ErrorCode.KAFKA_CHANGE_DOMAIN_STATUS_PRODUCER_ERROR);
        });
    }
}
