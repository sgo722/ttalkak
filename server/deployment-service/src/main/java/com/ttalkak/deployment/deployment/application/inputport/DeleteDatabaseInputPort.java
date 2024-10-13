package com.ttalkak.deployment.deployment.application.inputport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttalkak.deployment.common.global.error.ErrorCode;
import com.ttalkak.deployment.common.global.exception.BusinessException;
import com.ttalkak.deployment.deployment.application.outputport.*;
import com.ttalkak.deployment.deployment.application.usecase.DeleteDatabaseUseCase;
import com.ttalkak.deployment.deployment.domain.event.DeleteDatabaseEvent;
import com.ttalkak.deployment.deployment.domain.model.DatabaseEntity;
import com.ttalkak.deployment.deployment.framework.kafka.DeleteEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DeleteDatabaseInputPort implements DeleteDatabaseUseCase {

    private final DomainOutputPort domainOutputPort;

    private final DatabaseOutputPort databaseOutputPort;

    private final DeleteEventProducer deleteEventProducer;

    @Override
    public void deleteDatabase(Long userId, Long databaseId) {
        DatabaseEntity databaseEntity = databaseOutputPort.findById(databaseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXISTS_DATABASE));

        if(!databaseEntity.getUserId().equals(userId)){
            throw new BusinessException(ErrorCode.UN_AUTHORIZATION);
        }

        domainOutputPort.deleteDomainKey("database_" + databaseEntity.getId());

        try {
            deleteEventProducer.occurDeleteDatabase(new DeleteDatabaseEvent(databaseEntity.getId()));
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.KAFKA_PRODUCER_ERROR);
        }

        databaseOutputPort.delete(databaseEntity);
    }
}
