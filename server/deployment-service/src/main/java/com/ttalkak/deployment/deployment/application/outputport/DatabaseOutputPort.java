package com.ttalkak.deployment.deployment.application.outputport;

import com.ttalkak.deployment.deployment.domain.model.DatabaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DatabaseOutputPort {

    DatabaseEntity save(DatabaseEntity databaseEntity);

    List<DatabaseEntity> findAllByUserId(Long userId);

    Optional<DatabaseEntity> findById(Long databaseId);

    Page<DatabaseEntity> findAllByPaging(Pageable pageable, Long userId);

    Page<DatabaseEntity> findDatabaseContainsSearchKeyWord(Pageable pageable, Long userId, String searchKeyword);

    void delete(DatabaseEntity databaseEntity);

}
