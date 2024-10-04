package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.deployment.framework.web.request.DatabaseCreateRequest;
import com.ttalkak.deployment.deployment.framework.web.response.DatabaseResponse;


public interface CreateDatabaseUseCase {

    DatabaseResponse createDatabase(Long userId, DatabaseCreateRequest databaseCreateRequest);
}
