package com.ttalkak.project.project.framework.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttalkak.project.project.application.usecase.DeleteProjectUseCase;
import com.ttalkak.project.project.domain.event.DeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteTransactionConsumer {

    private final ObjectMapper objectMapper;

    private final DeleteProjectUseCase deleteProjectUseCase;
    /**
     * 프로젝트 삭제 과정중에 에러 발생시 롤백 처리한다.
     * @param record
     * @throws IOException
     */
    @KafkaListener(topics = "${consumers.topics.delete-project-exception.name}", groupId = "${consumers.group-id.project-deletion-group.name}")
    public void consumeRollBack(ConsumerRecord<String, String> record) throws IOException {
        log.info("consume roll back:{}", record.value());
        DeletedEvent deletedEvent = objectMapper.readValue(record.value(), DeletedEvent.class);
        deleteProjectUseCase.rollbackStatusProject(deletedEvent.getProjectId());
    }
}
