package com.ttalkak.deployment.deployment.application.inputport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttalkak.deployment.common.global.error.ErrorCode;
import com.ttalkak.deployment.common.global.exception.BusinessException;
import com.ttalkak.deployment.common.global.exception.EntityNotFoundException;
import com.ttalkak.deployment.deployment.application.outputport.DatabaseOutputPort;
import com.ttalkak.deployment.deployment.application.usecase.CommandDatabaseStatusUseCase;
import com.ttalkak.deployment.deployment.domain.event.UpdateDatabaseStatusEvent;
import com.ttalkak.deployment.deployment.domain.event.UpdateDeploymentStatusEvent;
import com.ttalkak.deployment.deployment.domain.model.DatabaseEntity;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;
import com.ttalkak.deployment.deployment.domain.model.vo.Status;
import com.ttalkak.deployment.deployment.framework.kafka.ChangeDatabaseStatusProducer;
import com.ttalkak.deployment.deployment.framework.kafka.ChangeDeploymentStatusProducer;
import com.ttalkak.deployment.deployment.framework.web.request.DatabaseCommandStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
@Transactional
public class CommandDatabaseStatusInputPort implements CommandDatabaseStatusUseCase {



    private final DatabaseOutputPort databaseOutputPort;

    private final ChangeDatabaseStatusProducer changeDatabaseStatusProducer;


    @Override
    public void commandDatabaseStatus(Long userId, DatabaseCommandStatusRequest databaseCommandStatusRequest){
        DatabaseEntity databaseEntity = databaseOutputPort.findById(Long.valueOf(databaseCommandStatusRequest.getDatabaseId()))
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_EXISTS_DEPLOYMENT));

        if(!Objects.equals(userId, databaseEntity.getUserId())){
            throw new BusinessException(ErrorCode.UN_AUTHORIZATION);
        }

        databaseEntity.updateStatus(Status.PENDING);

        UpdateDatabaseStatusEvent updateDeploymentStatusEvent = toKafkaEventMessage(databaseCommandStatusRequest);
        try{
            changeDatabaseStatusProducer.occurUpdateDatabaseStatus(updateDeploymentStatusEvent);
        }catch (JsonProcessingException e){
            throw new BusinessException(ErrorCode.KAFKA_PRODUCER_ERROR);
        }

        databaseOutputPort.save(databaseEntity);
    }

    private UpdateDatabaseStatusEvent toKafkaEventMessage(DatabaseCommandStatusRequest databaseCommandStatusRequest) {
        UpdateDatabaseStatusEvent updateDatabaseStatusEvent = new UpdateDatabaseStatusEvent(
                databaseCommandStatusRequest.getDatabaseId(),
                ServiceType.DATABASE,
                databaseCommandStatusRequest.getCommand()
        );
        return updateDatabaseStatusEvent;
    }

}
