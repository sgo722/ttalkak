package com.ttalkak.deployment.deployment.framework.jpaadapter.adapter
        ;

import com.ttalkak.deployment.deployment.application.outputport.DatabaseOutputPort;
import com.ttalkak.deployment.deployment.domain.model.DatabaseEntity;
import com.ttalkak.deployment.deployment.framework.jpaadapter.repository.DatabaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DatabaseAdapter implements DatabaseOutputPort {

    private final DatabaseRepository databaseRepository;
    @Override
    public DatabaseEntity save(DatabaseEntity databaseEntity) {
        DatabaseEntity savedDatabaseEntity = databaseRepository.save(databaseEntity);
        return savedDatabaseEntity;
    }
}
