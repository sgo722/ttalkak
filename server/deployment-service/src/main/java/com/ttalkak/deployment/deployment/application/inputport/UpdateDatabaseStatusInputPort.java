package com.ttalkak.deployment.deployment.application.inputport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttalkak.deployment.common.UseCase;
import com.ttalkak.deployment.common.global.error.ErrorCode;
import com.ttalkak.deployment.common.global.exception.BusinessException;
import com.ttalkak.deployment.deployment.application.outputport.DatabaseOutputPort;
import com.ttalkak.deployment.deployment.application.outputport.DomainOutputPort;
import com.ttalkak.deployment.deployment.application.outputport.EventOutputPort;
import com.ttalkak.deployment.deployment.application.usecase.UpdateDatabaseStatusUseCase;
import com.ttalkak.deployment.deployment.domain.event.*;
import com.ttalkak.deployment.deployment.domain.model.DatabaseEntity;
import com.ttalkak.deployment.deployment.domain.model.vo.Status;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.DatabaseDomainKeyRequest;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.DatabaseDomainKeyResponse;
import com.ttalkak.deployment.deployment.framework.web.request.DatabaseUpdateStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.ttalkak.deployment.deployment.domain.model.vo.Status.*;

@RequiredArgsConstructor
@UseCase
@Transactional
public class UpdateDatabaseStatusInputPort implements UpdateDatabaseStatusUseCase {

    private final DatabaseOutputPort databaseOutputPort;

    private final DomainOutputPort domainOutputPort;

    private final EventOutputPort eventOutputPort;

    @Override
    public void updateDatabaseStatus(DatabaseUpdateStatusRequest databaseUpdateStatusRequest) {
        Long databaseId = Long.parseLong(databaseUpdateStatusRequest.getDatabaseId());

        DatabaseEntity databaseEntity = databaseOutputPort.findById(databaseId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_EXISTS_DATABASE)
        );

        Status status = databaseEntity.getStatus();
        String message = databaseUpdateStatusRequest.getMessage();

        if(status == WAITING) {
            if (message.equals("cloud manipulate")) {
                reAllocateDatabase(databaseEntity);
            }
            databaseEntity.setStatus(WAITING);
        }

        if(status == ERROR){
            databaseEntity.setStatus(ERROR);
        }

        if(status == STOPPED){
            databaseEntity.setStatus(status);
        }

        if(status == RUNNING){
            databaseEntity.setStatus(status);
        }
        databaseEntity.setStatusMessage(message);
        databaseOutputPort.save(databaseEntity);


    }

    private void reAllocateDatabase(DatabaseEntity databaseEntity) {
        domainOutputPort.deleteDomainKey("database_" + databaseEntity.getId());

        DatabaseDomainKeyResponse databaseDomainKeyResponse = domainOutputPort.makeDatabaseDomainKey(new DatabaseDomainKeyRequest(
                "database_" + databaseEntity.getId(),
                "database " + databaseEntity.getId() + " " + databaseEntity.getDatabaseType(),
                "database " + databaseEntity.getId() + " " + databaseEntity.getDatabaseType()
        ));
        int port = databaseDomainKeyResponse.getPort();

        databaseEntity.setPort(port);
        DatabaseEntity savedDatabase = databaseOutputPort.save(databaseEntity);


        DatabaseEvent databaseEvent = new DatabaseEvent(
                savedDatabase.getDatabaseType(),
                savedDatabase.getName(),
                savedDatabase.getUsername(),
                savedDatabase.getPassword()
        );

        CreateDatabaseEvent createDatabaseEvent = new CreateDatabaseEvent(
                savedDatabase.getId(),
                databaseDomainKeyResponse.getKey(),
                databaseEvent,
                savedDatabase.getPort()
        );

        try {
            eventOutputPort.occurCreateDatabase(createDatabaseEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("카프카 요청 오류가 발생했습니다.");
        }
    }
}
