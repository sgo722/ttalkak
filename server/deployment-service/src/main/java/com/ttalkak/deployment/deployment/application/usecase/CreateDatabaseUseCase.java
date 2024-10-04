package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.deployment.framework.web.request.DatabaseCreateRequest;
import com.ttalkak.deployment.deployment.framework.web.response.DatabaseDetailResponse;
import com.ttalkak.deployment.deployment.framework.web.response.DatabasePreviewResponse;


public interface CreateDatabaseUseCase {

    DatabasePreviewResponse createDatabase(Long userId, DatabaseCreateRequest databaseCreateRequest);
}
