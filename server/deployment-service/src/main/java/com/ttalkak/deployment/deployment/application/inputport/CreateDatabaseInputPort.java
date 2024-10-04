package com.ttalkak.deployment.deployment.application.inputport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttalkak.deployment.deployment.application.outputport.DatabaseOutputPort;
import com.ttalkak.deployment.deployment.application.outputport.DomainOutputPort;
import com.ttalkak.deployment.deployment.application.outputport.EventOutputPort;
import com.ttalkak.deployment.deployment.application.usecase.CreateDatabaseUseCase;
import com.ttalkak.deployment.deployment.domain.event.CreateDatabaseEvent;
import com.ttalkak.deployment.deployment.domain.event.DatabaseEvent;
import com.ttalkak.deployment.deployment.domain.model.DatabaseEntity;
import com.ttalkak.deployment.deployment.domain.model.vo.DatabaseType;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.DatabaseDomainKeyRequest;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.DatabaseDomainKeyResponse;
import com.ttalkak.deployment.deployment.framework.web.request.DatabaseCreateRequest;
import com.ttalkak.deployment.deployment.framework.web.response.DatabaseDetailResponse;
import com.ttalkak.deployment.deployment.framework.web.response.DatabasePreviewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CreateDatabaseInputPort implements CreateDatabaseUseCase {

    private final DomainOutputPort domainOutputPort;

    private final DatabaseOutputPort databaseOutputPort;

    private final EventOutputPort eventOutputPort;

    @Override
    public DatabasePreviewResponse createDatabase(Long userId, DatabaseCreateRequest databaseCreateRequest) {
        DatabaseType type = databaseCreateRequest.getType();
        String name = databaseCreateRequest.getName();

        DatabaseEntity database = DatabaseEntity.createDatabase(userId, name, type);
        DatabaseEntity saveDatabase = databaseOutputPort.save(database);

        DatabaseDomainKeyResponse databaseDomainKeyResponse = domainOutputPort.makeDatabaseDomainKey(new DatabaseDomainKeyRequest(
                "database_" + database.getId(),
                "database " + database.getId() + " " + database.getDatabaseType(),
                "database " + database.getId() + " " + database.getDatabaseType()
        ));
        int port = databaseDomainKeyResponse.getPort();

        saveDatabase.setPort(port);
        DatabaseEntity savedDatabase = databaseOutputPort.save(saveDatabase);


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

        return DatabasePreviewResponse.mapToDTO(savedDatabase);
    }
}
