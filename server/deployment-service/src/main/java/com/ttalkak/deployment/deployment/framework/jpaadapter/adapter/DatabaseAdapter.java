package com.ttalkak.deployment.deployment.framework.jpaadapter.adapter;

import com.ttalkak.deployment.deployment.application.outputport.DatabaseOutputPort;
import com.ttalkak.deployment.deployment.domain.model.DatabaseEntity;
import com.ttalkak.deployment.deployment.framework.jpaadapter.repository.DatabaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DatabaseAdapter implements DatabaseOutputPort {

    private final DatabaseRepository databaseRepository;

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
    public Optional<DatabaseEntity> findById(Long databaseId) {
        return databaseRepository.findById(databaseId);
    }

    @Override
    public void delete(DatabaseEntity databaseEntity) {
        databaseRepository.delete(databaseEntity);
    }

    @Override
    public Page<DatabaseEntity> findAllByPaging(Pageable pageable, Long userId) {
        return databaseRepository.findAllByPaging(pageable, userId);
    }

    @Override
    public Page<DatabaseEntity> findDatabaseContainsSearchKeyWord(Pageable pageable, Long userId, String searchKeyword) {
        return databaseRepository.findDatabaseContainsSearchKeyWord(pageable, userId, searchKeyword);
    }


}
