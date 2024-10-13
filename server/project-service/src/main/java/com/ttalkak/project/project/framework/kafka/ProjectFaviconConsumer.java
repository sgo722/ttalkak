package com.ttalkak.project.project.framework.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttalkak.project.project.application.usecase.DeleteProjectUseCase;
import com.ttalkak.project.project.application.usecase.UpdateProjectUseCase;
import com.ttalkak.project.project.domain.event.DeletedEvent;
import com.ttalkak.project.project.domain.event.FaviconEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectFaviconConsumer {
    private final ObjectMapper objectMapper;
    private final UpdateProjectUseCase updateProjectUseCase;

    @KafkaListener(topics = "${consumers.topics.update-project-favicon.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeRollBack(ConsumerRecord<String, String> record) throws IOException {
        FaviconEvent faviconEvent = objectMapper.readValue(record.value(), FaviconEvent.class);
        updateProjectUseCase.updateProjectFavicon(faviconEvent.getProjectId(), faviconEvent.getFaviconUrl());
    }
}
