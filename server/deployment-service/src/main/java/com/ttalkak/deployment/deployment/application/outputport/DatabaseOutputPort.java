package com.ttalkak.deployment.deployment.application.outputport;

import com.ttalkak.deployment.deployment.domain.model.DatabaseEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseOutputPort {

    public DatabaseEntity save(DatabaseEntity databaseEntity);
}
