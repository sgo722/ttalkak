package com.ttalkak.deployment.deployment.framework.jpaadapter.adapter
        ;

import com.ttalkak.deployment.deployment.application.outputport.DatabaseOutputPort;
import com.ttalkak.deployment.deployment.domain.model.DatabaseEntity;
import com.ttalkak.deployment.deployment.framework.jpaadapter.repository.DatabaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DatabaseAdapter implements DatabaseOutputPort {

    private final DatabaseRepository databaseRepository;
    private final DatabaseOutputPort databaseOutputPort;

    @Override
    public DatabaseEntity save(DatabaseEntity databaseEntity) {
        DatabaseEntity savedDatabaseEntity = databaseRepository.save(databaseEntity);
        return savedDatabaseEntity;
    }

    @Override
    public List<DatabaseEntity> findAllByUserId(Long userId) {
        return databaseRepository.findAllByUserId(userId);
    }

    @Override
    public DatabaseEntity findById(Long databaseId) {
        return databaseRepository.findByDatabaseId(databaseId);
    }
}
