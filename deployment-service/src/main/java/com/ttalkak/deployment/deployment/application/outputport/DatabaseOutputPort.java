package com.ttalkak.deployment.deployment.application.outputport;

import com.ttalkak.deployment.deployment.domain.model.DatabaseEntity;
import com.ttalkak.deployment.deployment.domain.model.DeploymentEntity;
import com.ttalkak.deployment.deployment.domain.model.VersionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseOutputPort {

    DatabaseEntity save(DatabaseEntity databaseEntity);

    List<DatabaseEntity> findAllByUserId(Long userId);

    DatabaseEntity findById(Long databaseId);
}
