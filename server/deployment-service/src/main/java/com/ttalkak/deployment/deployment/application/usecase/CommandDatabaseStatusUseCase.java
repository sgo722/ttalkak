package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.deployment.framework.web.request.DatabaseCommandStatusRequest;
import com.ttalkak.deployment.deployment.framework.web.request.DeploymentCommandStatusRequest;

public interface CommandDatabaseStatusUseCase {

    public void commandDatabaseStatus(Long userId, DatabaseCommandStatusRequest databaseCommandStatusRequest);
}
