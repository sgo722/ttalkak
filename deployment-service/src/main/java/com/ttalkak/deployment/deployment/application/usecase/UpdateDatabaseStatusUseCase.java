package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.deployment.framework.web.request.DatabaseUpdateStatusRequest;

public interface UpdateDatabaseStatusUseCase {

    void updateDatabaseStatus(DatabaseUpdateStatusRequest databaseUpdateStatusRequest);
}
